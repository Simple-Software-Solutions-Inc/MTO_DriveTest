package ca.lambton.Wildemo.Models;

import java.util.HashMap;
import java.util.Map;

public class Url {
	
	
	private Map<String, String> url = new HashMap<String, String>();
	
	public Map<String, String> getUrl(){
		return url;
	}
	
	private void addUrl(String key, String value){
		 url.put(key, value);
	}
	
	public Url() {
		//site map
		addUrl("Home", "/");
		addUrl("Registration", "registration");
		addUrl("Reports", "reports");
		addUrl("Store", "store");
		addUrl("Checkout", "store/checkout");
		addUrl("Main", "main");
		addUrl("Dashboard", "dashboard");
		addUrl("products", "dashboard/products");
		addUrl("Products_add-new", "dashboard/products/add-new");
		addUrl("Products_edit", "dashboard/products/edit");
		addUrl("products_view", "dashboard/products/view");
		addUrl("products_delete", "dashboard/products/delete");
		
		addUrl("categories", "dashboard/categories");
		addUrl("categories_add-new", "dashboard/categories/add-new");
		addUrl("categories_edit", "dashboard/categories/edit");
		addUrl("categories_view", "dashboard/categories/view");
		addUrl("categories_delete", "dashboard/categories/delete");
		
		addUrl("questions", "dashboard/questions");
		addUrl("questions_add-new", "dashboard/questions/add-new");
		addUrl("questions_edit", "dashboard/questions/edit");
		addUrl("questions_view", "dashboard/questions/view");
		addUrl("questions_delete", "dashboard/questions/delete");
		
		addUrl("question-categories", "dashboard/question-categories");
	}
}
