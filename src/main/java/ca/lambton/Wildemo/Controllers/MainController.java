package ca.lambton.Wildemo.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import ca.lambton.Wildemo.Models.Utilities;
import ca.lambton.Wildemo.Models.WIL.Category;
import ca.lambton.Wildemo.Models.WIL.Question;
import ca.lambton.Wildemo.Models.WIL.Question_Category;
import ca.lambton.Wildemo.Repositories.WIL.CategoryRepository;
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



	

	// dashboard
	@GetMapping("/dashboard")
	public String signUp(Model model) {
		List<String[]> modelNames = allModel().keySet().stream().map(x -> {
			String[] str = { x.toString(), Utilities.slug(x.toString()) };
			return str;
		}).collect(Collectors.toList());

		model.addAttribute("modelNames", modelNames);
		model.addAttribute("modelData", null);
		return "layouts/dashboard_components/dashboard";
	}

	// Dynamically load each model dashboard
	@GetMapping("/dashboard/{modelName}")
	public String signUp(@PathVariable("modelName") String modelName, Model model) {
		List<String[]> modelNames = allModel().keySet().stream().map(x -> {
			String[] str = { x.toString(), Utilities.slug(x.toString()) };
			return str;
		}).collect(Collectors.toList());

		var modelData = allModel().get(Utilities.unSlug(modelName));
		System.out.println(modelName);
		model.addAttribute("modelData", modelData);
		model.addAttribute("currentModelName", Utilities.unSlug(modelName));
		model.addAttribute("modelNames", modelNames);

		return "layouts/dashboard_components/dashboard";
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

	@GetMapping("/app/{modelName}/search")
	public String products(@PathVariable("modelName") String modelName, @RequestParam("search_id") String id,
			Model model) {

		List<Question_Category> lq1 = questionCategoryDb.findAll().stream().filter(x-> (x.getCategoryId().getCat_id())== Integer.parseInt(id)).collect(Collectors.toList());
		List<Question> lq = questionDb.findAll().stream().filter(x-> {
			boolean t = false;
			for (Question_Category c : lq1) {
				if (c.getQuestionId().getQues_id() == x.getQues_id())
				t= true;
			}
			return t;
		} 
		).collect(Collectors.toList());
		
		ModelMap modelData = (ModelMap) allModel().get(Utilities.unSlug(modelName));
		modelData = fixModel(modelData, Utilities.unSlug(modelName), id);
//		model.addAttribute("modelData", modelData);
//		model.addAttribute("modIds", categoryDb.findAll());
		model.addAttribute("modelData", lq);
		model.addAttribute("currentModelName", Utilities.unSlug(modelName));
		model.addAttribute("filter", id);

//		return "layouts/table_components/main_table";
		return "driveTest/admin";
	}

//	get detail
	@GetMapping("/app/{modelName}/view/{id}")
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

			return "layouts/table_components/view_details";
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
	@GetMapping("/app/{modelName}/delete/{id}")
	public String deleteModelObject(@PathVariable("id") Integer id, @PathVariable("modelName") String modelName,
			Model model) {

		ModelMap modelData = (ModelMap) allModel().get(Utilities.unSlug(modelName));

		modelData = fixModel(modelData, Utilities.unSlug(modelName), id.toString());

		if (modelData.get(Utilities.unSlug(modelName)) != null) {

			model.addAttribute("modelClass", modelData.get(Utilities.unSlug(modelName)));
			model.addAttribute("prod_status", "delete");
			model.addAttribute("modelName", modelName);
			model.addAttribute("modelId", id);

//			pageCounter.increment();

			return "layouts/table_components/view_details";
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
				
				
			case "question categories": {
				List<Question_Category> p = (List<Question_Category>) modelData.get(modelName);
				Question_Category o = p.stream().filter(g -> g.getQuesCatId() == Integer.parseInt(id))
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
