package ca.lambton.Wildemo.Controllers;

import java.util.List;
import java.util.stream.Collectors;

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
import ca.lambton.Wildemo.Repositories.WIL.CategoryRepository;
import ca.lambton.Wildemo.Repositories.WIL.ProductRepository;
import ca.lambton.Wildemo.Repositories.WIL.QuestionCategoryRepository;
import ca.lambton.Wildemo.Repositories.WIL.QuestionRepository;
import lombok.var;

@Controller
public class MainController {


	
	
	
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

	

	// dashboard
	@GetMapping("/dashboard")
	public String signUp(Model model) {
		List<String[]> modelNames = allModel().keySet().stream().map(x -> {
			String[] str = { x.toString(), Utilities.slug(x.toString()) };
			return str;
		}).collect(Collectors.toList());

		model.addAttribute("modelNames", modelNames);
		model.addAttribute("modelData", null);
//		return "layouts/dashboard_components/dashboard";
		
		breadCrumbs.start("Dashboard");
		model.addAttribute("links", breadCrumbs);
		return "driveTest/dashboard";
	}

	// Dynamically load each model dashboard
	@GetMapping("/dashboard/{modelName}")
	public String signUp(@PathVariable("modelName") String modelName, Model model) {
		List<String[]> modelNames = allModel().keySet().stream().map(x -> {
			String[] str = { x.toString(), Utilities.slug(x.toString()) };
			return str;
		}).collect(Collectors.toList());

		
		var modelData = allModel().get(Utilities.unSlug(modelName));
		ModelMap model1 = (ModelMap) modelData;
		System.out.println(modelName);
		model.addAttribute("modelData", modelData);
		model.addAttribute("currentModelName", Utilities.unSlug(modelName));
		model.addAttribute("modelNames", modelNames);
		model.addAttribute("modIds", model1.get("modIds"));
		
		breadCrumbs.start(modelName);
//		breadCrumbs.start("Products");
		model.addAttribute("links", breadCrumbs);
		return "driveTest/table";
	}

	// Dynamically return the built table for each model
	@GetMapping("/app/{modelName}")
	public String getModel(@PathVariable("modelName") String modelName, Model model) {
		System.out.println(modelName);
		var modelData = allModel().get(Utilities.unSlug(modelName));

		model.addAttribute("modelData", modelData);
		model.addAttribute("currentModelName", Utilities.unSlug(modelName));
		return "layouts/table_components/main_table";
	}

	@GetMapping("/dashboard/{modelName}/search")
	public String products(@PathVariable("modelName") String modelName, @RequestParam("search_id") String id,
			Model model) {

//		List<Question_Category> lq1 = questionCategoryDb.findAll().stream().filter(x-> (x.getCategoryId().getCat_id())== Integer.parseInt(id)).collect(Collectors.toList());
//		List<Question> lq = questionDb.findAll().stream().filter(x-> {
//			boolean t = false;
//			for (Question_Category c : lq1) {
//				if (c.getQuestionId().getQues_id() == x.getQues_id())
//				t= true;
//			}
//			return t;
//		} 
//		).collect(Collectors.toList());
		
		ModelMap modelData = (ModelMap) allModel().get(Utilities.unSlug(modelName));
		modelData = fixModel(modelData, Utilities.unSlug(modelName), id);
		model.addAttribute("modelData", modelData);
		model.addAttribute("modIds", modelData.get("modIds"));
//		model.addAttribute("modelData", lq);
		model.addAttribute("currentModelName", Utilities.unSlug(modelName));
		model.addAttribute("filter", id);

//		return "layouts/table_components/main_table";
		return "driveTest/table";
	}

//	get detail
	@GetMapping("/dashboard/{modelName}/view/{id}")
	public String getModelObjectDetail(@PathVariable("id") Integer id, @PathVariable("modelName") String modelName,
			Model model) {

		ModelMap modelData = (ModelMap) allModel().get(Utilities.unSlug(modelName));

		modelData = fixModel(modelData, Utilities.unSlug(modelName), id.toString());

		if (modelData.get(Utilities.unSlug(modelName)) != null) {

			model.addAttribute("modelClass", modelData.get(Utilities.unSlug(modelName)));
			model.addAttribute("prod_status", "detail");
			model.addAttribute("modelName", modelName);
			model.addAttribute("modelId", id);
//			model.addAttribute("modelNav", new Navigate());

			//Breadcrumbs
			breadCrumbs.start(modelName + "_view");
			model.addAttribute("links", breadCrumbs);
			return "driveTest/view_detail";
		}
		return "error";
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

			return "driveTest/view_detail";
		}
		return "error";
	}
	
	@PostMapping("/dashboard/products/delete/{id}")
	public String deleteProduct(@PathVariable("id") Integer id) {

		if (productDb.findById(id).isPresent()) {
			Product product = productDb.findById(id).get();
			productDb.delete(product);
			return "redirect:/dashboard/products";
		}
		return "error";
	}
	
	@PostMapping("/dashboard/categories/delete/{id}")
	public String deleteCategory(@PathVariable("id") Integer id) {

		if (categoryDb.findById(id).isPresent()) {
			Category category = categoryDb.findById(id).get();
			categoryDb.delete(category);
			return "redirect:/dashboard/products";
		}
		return "error";
	}
	
	@PostMapping("/dashboard/questions/delete/{id}")
	public String deleteQuestions(@PathVariable("id") Integer id) {

		if (questionDb.findById(id).isPresent()) {
			Question question = questionDb.findById(id).get();
			questionDb.delete(question);
			return "redirect:/dashboard/questions";
		}
		return "error";
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	@PostMapping("/dashboard/{modelName}/view/{id}")
	public String getModelObjectDetail(@Valid Navigate navigate, @PathVariable("modelName") String modelName) {
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
	
	// store board
		@GetMapping("/store")
		public String shoreStore(Model model) {
			List<Product> storeproducts = productDb.findAll();

			model.addAttribute("storeproducts", storeproducts);
			model.addAttribute("modelData", null);
//			return "layouts/dashboard_components/dashboard";
			//Breadcrumbs
//			breadCrumbs.resetBreadCrumbs();
			breadCrumbs.start("Store");
			model.addAttribute("links", breadCrumbs);
			return "driveTest/store";
		}

}
