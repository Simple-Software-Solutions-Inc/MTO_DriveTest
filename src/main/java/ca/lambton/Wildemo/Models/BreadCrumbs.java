package ca.lambton.Wildemo.Models;


import java.util.LinkedHashMap;
import java.util.Map;

public class BreadCrumbs {

	private Map<String, String> breadCrumbs = new LinkedHashMap<String, String>();
	
	public void start(String key) {
		breadCrumbs.clear();
		Url url = new Url();
		String urlpath = url.getUrl().get(key);
		String[] pathList = urlpath.split("/");
		if (pathList.length == 0) {
			initialize();
		}
		else {
			String keyvalue = "";
			for (String value : pathList) {
//				String key = value;
				keyvalue += "/" + value;
				addBreadCrumbs(value, keyvalue);
			}
		}
		
		
	}
	private void initialize() {
		breadCrumbs.put("Home", "/");
	}
	
	public void addBreadCrumbs(String key, String value) {
		if(breadCrumbs.size()==0) {
			initialize();
		}
		breadCrumbs.put(key, value);
	}
	
	public void removeBreadCrumbs(String key) {
		breadCrumbs.remove(key);
	}
	
	public void resetBreadCrumbs() {
		breadCrumbs.clear();
	}
	
	public Map<String, String> get() {
		return breadCrumbs;
	}
}
