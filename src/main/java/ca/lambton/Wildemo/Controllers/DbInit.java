package ca.lambton.Wildemo.Controllers;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.lambton.Wildemo.Models.Utilities;
import ca.lambton.Wildemo.Models.WIL.Admin;
import ca.lambton.Wildemo.Models.WIL.AdminPassword;
import ca.lambton.Wildemo.Models.WIL.Question;
import ca.lambton.Wildemo.Repositories.WIL.AdminPasswordRepository;
import ca.lambton.Wildemo.Repositories.WIL.AdminRepository;
import ca.lambton.Wildemo.Repositories.WIL.QuestionRepository;

@Component
public class DbInit {

	@Autowired
	private AdminRepository adminDb;

	@Autowired
	private AdminPasswordRepository adminPasswordDb;
	
	@Autowired
	private QuestionRepository questionDb;
	
    @PostConstruct
    private void postConstruct() {
		final String ADMIN_ID = "admin@admin.com";
		final String ADMIN_PWD = "admin";
		
//		@Value("${paypal.client.id}")
//		private String clientId;
//		@Value("${paypal.client.secret}")
//		private String clientSecret;
//		@Value("${paypal.mode}")
//		private String mode;
		
		//ensures the supper user admin account is always created
		if (adminDb.findAll().stream().filter(x -> x.getAdminRole().equals("Admin") || x.getAdminEmail().equals(ADMIN_ID) || x.getAdminName().equals("Super User")).count() == 0) {
			Admin admin = new Admin();
			admin.setAdminName("Super User");
			admin.setAdminRole("Admin");
			admin.setAdminEmail(ADMIN_ID);
			admin.setAdminStatus("active");
			Admin o = adminDb.save(admin);

			AdminPassword adminPwd = new AdminPassword();
			adminPwd.setAdminId(o);
			adminPwd.setPassword(Utilities.getMd5(ADMIN_PWD));
			adminPwd.setExpiryDate(Utilities.pwdExpiryDate());
			adminPasswordDb.save(adminPwd);
		}
		
		//ensures that the question table gets populated with the questions from the json file
		if (questionDb.count() == 0) {
			for (Question m : Utilities.loadQuestions()) {
				questionDb.save(m);
			}
			if (questionDb.count() == 0) {
				System.out.println("No questions were loaded");
			}
		}	
	}
}