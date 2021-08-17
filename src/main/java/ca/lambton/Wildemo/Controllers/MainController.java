package ca.lambton.Wildemo.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.lambton.Wildemo.Models.BreadCrumbs;
import ca.lambton.Wildemo.Models.Navigate;
import ca.lambton.Wildemo.Models.Utilities;
import ca.lambton.Wildemo.Models.WIL.Category;
import ca.lambton.Wildemo.Models.WIL.Product;
import ca.lambton.Wildemo.Models.WIL.Question;
import ca.lambton.Wildemo.Models.WIL.Question_Category;
import ca.lambton.Wildemo.Repositories.WIL.AdminRepository;
import ca.lambton.Wildemo.Repositories.WIL.ApplicantRepository;
import ca.lambton.Wildemo.Repositories.WIL.CategoryRepository;
import ca.lambton.Wildemo.Repositories.WIL.ProductRepository;
import ca.lambton.Wildemo.Repositories.WIL.QuestionCategoryRepository;
import ca.lambton.Wildemo.Repositories.WIL.QuestionRepository;
import lombok.var;

@Controller
public class MainController {


	
	@Autowired
	private ApplicantRepository applicantDb;

	@Autowired
	private AdminRepository adminDb;
	
	@Autowired
	private CategoryRepository categoryDb;
	
	@Autowired
	private QuestionCategoryRepository questionCategoryDb;
	
	@Autowired
	private QuestionRepository questionDb;
	
	@Autowired
	private ProductRepository productDb;

	@Autowired
	private BreadCrumbs breadCrumbs;

	@Autowired
	private HttpSession session;
	
 
	// dashboard
	@GetMapping("/dashboard")
	public String dashboard(Model model) {
		
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				List<String[]> modelNames = allModel().keySet().stream().map(x -> {
					String[] str = { x.toString(), Utilities.slug(x.toString()) };
					return str;
				}).collect(Collectors.toList());

				model.addAttribute("modelNames", modelNames);
				model.addAttribute("modelData", null);
				
				breadCrumbs.start("Dashboard");
				model.addAttribute("links", breadCrumbs);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				return "driveTest/dashboard";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
		
		
	}

	// Dynamically load each model dashboard
	@GetMapping("/dashboard/{modelName}")
	public String dashboardModel(@PathVariable("modelName") String modelName, Model model) {
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				try {
					List<String[]> modelNames = allModel().keySet().stream().map(x -> {
						String[] str = { x.toString(), Utilities.slug(x.toString()) };
						return str;
					}).collect(Collectors.toList());

					
					var modelData = allModel().get(Utilities.unSlug(modelName));
					ModelMap model1 = (ModelMap) modelData;
//					System.out.println(modelName);
					model.addAttribute("modelData", modelData);
					model.addAttribute("currentModelName", Utilities.unSlug(modelName));
					model.addAttribute("modelNames", modelNames);
					model.addAttribute("modIds", model1.get("modIds"));
					
					breadCrumbs.start(modelName);
					model.addAttribute("links", breadCrumbs);
					model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
					return "driveTest/table";
				}catch(NullPointerException e) {
					return "error";
				}
			}
			return "redirect:/";
		}
		return "redirect:/admin";
		}

	// Dynamically return the built table for each model
//	@GetMapping("/app/{modelName}")
//	public String getModel(@PathVariable("modelName") String modelName, Model model) {
//		System.out.println(modelName);
//		var modelData = allModel().get(Utilities.unSlug(modelName));
//
//		model.addAttribute("modelData", modelData);
//		model.addAttribute("currentModelName", Utilities.unSlug(modelName));
//		return "layouts/table_components/main_table";
//	}

	@GetMapping("/dashboard/{modelName}/search")
	public String products(@PathVariable("modelName") String modelName, @RequestParam("search_id") String id,
			Model model) {
		
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				ModelMap modelData = (ModelMap) allModel().get(Utilities.unSlug(modelName));
				modelData = fixModel(modelData, Utilities.unSlug(modelName), id);
				model.addAttribute("modelData", modelData);
				model.addAttribute("modIds", modelData.get("modIds"));
//				model.addAttribute("modelData", lq);
				model.addAttribute("currentModelName", Utilities.unSlug(modelName));
				model.addAttribute("filter", id);

				//Breadcrumbs
				breadCrumbs.start(Utilities.unSlug(modelName));
				model.addAttribute("links", breadCrumbs);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				return "driveTest/table";
			}
				return "redirect:/";
			}
			return "redirect:/admin";
		
	}

//	get detail
	@GetMapping("/dashboard/{modelName}/view/{id}")
	public String getModelObjectDetail(@PathVariable("id") Integer id, @PathVariable("modelName") String modelName,
			Model model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				ModelMap modelData = (ModelMap) allModel().get(Utilities.unSlug(modelName));

				modelData = fixModel(modelData, Utilities.unSlug(modelName), id.toString());

				if (modelData.get(Utilities.unSlug(modelName)) != null) {

					model.addAttribute("modelClass", modelData.get(Utilities.unSlug(modelName)));
					model.addAttribute("prod_status", "detail");
					model.addAttribute("modelName", modelName);
					model.addAttribute("modelId", id);
//					model.addAttribute("modelNav", new Navigate());

					//Breadcrumbs
					breadCrumbs.start(modelName + "_view");
					model.addAttribute("links", breadCrumbs);
					model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
					return "driveTest/view_detail";
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";		
	}

	/**
	 * Deletes an object from a model
	 * @param id
	 * @param modelName
	 * @param model
	 * @return
	 */
	@GetMapping("/dashboard/{modelName}/delete/{id}")
	public String deleteModelObject(@PathVariable("id") Integer id, @PathVariable("modelName") String modelName,
			Model model) {
		
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				ModelMap modelData = (ModelMap) allModel().get(Utilities.unSlug(modelName));

				modelData = fixModel(modelData, Utilities.unSlug(modelName), id.toString());

				if (modelData.get(Utilities.unSlug(modelName)) != null) {

					model.addAttribute("modelClass", modelData.get(Utilities.unSlug(modelName)));
					model.addAttribute("prod_status", "delete");
					model.addAttribute("modelName", modelName);
					model.addAttribute("modelId", id);

					//Breadcrumbs
					breadCrumbs.start(modelName + "_delete");
					model.addAttribute("links", breadCrumbs);
					model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));

					return "driveTest/view_detail";
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";	
		
	}
	
	@PostMapping("/dashboard/products/delete/{id}")
	public String deleteProduct(@PathVariable("id") Integer id) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				if (productDb.findById(id).isPresent()) {
					Product product = productDb.findById(id).get();
					productDb.delete(product);
					return "redirect:/dashboard/products";
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}
	
	@PostMapping("/dashboard/categories/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				if (categoryDb.findById(id).isPresent()) {
					Category category = categoryDb.findById(id).get();
					categoryDb.delete(category);
					return "redirect:/dashboard/categories";
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}
	
	@PostMapping("/dashboard/questions/delete/{id}")
	public String deleteQuestions(@PathVariable("id") Integer id) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				if (questionDb.findById(id).isPresent()) {
					Question question = questionDb.findById(id).get();
					questionDb.delete(question);
					return "redirect:/dashboard/questions";
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@PostMapping("/dashboard/{modelName}/view/{id}")
	public String getModelObjectDetail(@Valid Navigate navigate, @PathVariable("modelName") String modelName) {
		
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				int id = navigate.getModelObjectId();
				String btnDetail = navigate.getNext();

				ModelMap modelData = (ModelMap) allModel().get(Utilities.unSlug(modelName));

				if (modelData != null) {

					List<Integer> pint = (List<Integer>) modelData.get("modIds");
					int i = pint.indexOf(id);
					if (btnDetail.equals("Next") && pint.size() - 1 > i) {
						i++;
					} else if (btnDetail.equals("Previous") && i > 0) {
						i--;
					}
					return "redirect:/dashboard/" + modelName + "/view/" + pint.get(i);
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}
	

	// load all model objects
	public ModelMap allModel() {
		ModelMap model = new ModelMap();		
		
		ModelMap categoryModel = new ModelMap();
		categoryModel.addAttribute("categories", categoryDb.findAll());
		categoryModel.addAttribute("fieldNames", Category.fieldNames());
		categoryModel.addAttribute("currentModel", "categories");
		categoryModel.addAttribute("modIds",
				categoryDb.findAll().stream().map(x -> x.getCat_id()).collect(Collectors.toList()));
		model.addAttribute("categories", categoryModel);
		
		ModelMap questionModel = new ModelMap();
		questionModel.addAttribute("questions", questionDb.findAll());
		questionModel.addAttribute("fieldNames", Question.fieldNames());
		questionModel.addAttribute("currentModel", "questions");
		questionModel.addAttribute("modIds",
				questionDb.findAll().stream().map(x -> x.getQues_id()).collect(Collectors.toList()));
		model.addAttribute("questions", questionModel);
		
		
		ModelMap questionCategoryModel = new ModelMap();
		questionCategoryModel.addAttribute("question categories", questionCategoryDb.findAll());
		questionCategoryModel.addAttribute("fieldNames", Question_Category.fieldNames());
		questionCategoryModel.addAttribute("currentModel", "question-categories");
		questionCategoryModel.addAttribute("modIds",
				questionCategoryDb.findAll().stream().map(x -> x.getQuesCatId()).collect(Collectors.toList()));
		model.addAttribute("question categories", questionCategoryModel);
		
		
		ModelMap productModel = new ModelMap();
		productModel.addAttribute("products", productDb.findAll());
		productModel.addAttribute("fieldNames", Product.fieldNames());
		productModel.addAttribute("currentModel", "products");
		productModel.addAttribute("modIds",
				productDb.findAll().stream().map(x -> x.getProductId()).collect(Collectors.toList()));
		model.addAttribute("products", productModel);
		
		return model;
	}

	@SuppressWarnings("unchecked")
	public ModelMap fixModel(ModelMap modelData, String modelName, String id) {

		if (!id.equals("All")) {
			switch (modelName) {		
			case "categories": {
				List<Category> p = (List<Category>) modelData.get(modelName);
				Category o = p.stream().filter(g -> g.getCat_id() == Integer.parseInt(id))
						.findFirst().orElse(null);// .get();
				modelData.addAttribute(modelName, o);
			}
				break;
				
			case "questions": {
				List<Question> p = (List<Question>) modelData.get(modelName);
				Question o = p.stream().filter(g -> g.getQues_id() == Integer.parseInt(id))
						.findFirst().orElse(null);// .get();
				modelData.addAttribute(modelName, o);
			}
				break;
				
				
			case "question categories": {
				List<Question_Category> p = (List<Question_Category>) modelData.get(modelName);
				Question_Category o = p.stream().filter(g -> g.getQuesCatId() == Integer.parseInt(id))
						.findFirst().orElse(null);// .get();
				modelData.addAttribute(modelName, o);
			}
				break;
				
			case "products": {
				List<Product> p = (List<Product>) modelData.get(modelName);
				Product o = p.stream().filter(g -> g.getProductId() == Integer.parseInt(id))
						.findFirst().orElse(null);// .get();
				modelData.addAttribute(modelName, o);
			}
				break;
//			  default:
			}
		}
		return modelData;

	}
	
	

}
