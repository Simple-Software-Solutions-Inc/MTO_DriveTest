package ca.lambton.Wildemo.Models;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BradeCrumbsConfig {

	//Method creates a page counter bean dependency for the project
		@Bean
		public BreadCrumbs getBreadCrumbs() {
			return new BreadCrumbs();
		}
}
