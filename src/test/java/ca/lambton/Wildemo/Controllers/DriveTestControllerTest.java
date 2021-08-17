package ca.lambton.Wildemo.Controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.lambton.Wildemo.AdditionalFunction.SmtpMailSender;
import ca.lambton.Wildemo.Models.BreadCrumbs;
import ca.lambton.Wildemo.Models.Utilities;
import ca.lambton.Wildemo.Models.WIL.Applicant;
import ca.lambton.Wildemo.Models.WIL.Location;
import ca.lambton.Wildemo.Models.WIL.MultipleChoice;
import ca.lambton.Wildemo.Models.WIL.Password;
import ca.lambton.Wildemo.Models.WIL.Prospect;
import ca.lambton.Wildemo.Models.WIL.ProspectLogin;
import ca.lambton.Wildemo.Models.WIL.Question;
import ca.lambton.Wildemo.Repositories.WIL.AdminPasswordRepository;
import ca.lambton.Wildemo.Repositories.WIL.AdminRepository;
import ca.lambton.Wildemo.Repositories.WIL.ApplicantQuizRepository;
import ca.lambton.Wildemo.Repositories.WIL.ApplicantRepository;
import ca.lambton.Wildemo.Repositories.WIL.CategoryRepository;
import ca.lambton.Wildemo.Repositories.WIL.LocationRepository;
import ca.lambton.Wildemo.Repositories.WIL.PasswordRepository;
import ca.lambton.Wildemo.Repositories.WIL.ProductRepository;
import ca.lambton.Wildemo.Repositories.WIL.ProofIdentityRepository;
import ca.lambton.Wildemo.Repositories.WIL.QuestionCategoryRepository;
import ca.lambton.Wildemo.Repositories.WIL.QuestionRepository;

@WebMvcTest(DriveTestController.class)
class DriveTestControllerTest {

	@MockBean
	SmtpMailSender smtpMailSender;
	
//	@MockBean
//	 HttpSession session;

	@MockBean
	 private ApplicantRepository applicantDb;
	
	@MockBean
	 AdminRepository adminDb;


	@MockBean
	 QuestionRepository questionDb;

	@MockBean
	 CategoryRepository categoryDb;

	@MockBean
	 LocationRepository locationDb;

	@MockBean
	 ProofIdentityRepository proofIdentityDb;

	@MockBean
	private PasswordRepository passwordDb;
	
	@MockBean
	 AdminPasswordRepository adminPasswordDb;
	
	@MockBean
	 ProductRepository productDb;

	@MockBean
	 ApplicantQuizRepository applicantQuizDb;
	
	@MockBean
	QuestionCategoryRepository questionCategoryDb;
	
	@MockBean
	 BreadCrumbs breadCrumbs;
	
	@Autowired
	 MockMvc mockMvc;
	
	private final String TEST_EMAIL = "dvn.shakes@gmail.com";
	
	private HashMap<String, Object> sessionattr = new HashMap<String, Object>();
	private ObjectMapper objectMapper = new ObjectMapper();
	private ModelMap modelSession = new ModelMap();
	
	//Testing the login get request for the drive test app
//	@Test
//	void testDriveTestModel() throws Exception{
//		
//		this.mockMvc.perform(get("/drive_test"))
//		.andDo(print())
//		.andExpect(status().isOk())
//		.andExpect(model().attribute("prospectLogin", new ProspectLogin()))
//		.andExpect(view().name("driveTest/login"));
//	}
	
	
	//Testing the login post request for the drive test app
//	 @Test
//	 void testDriveTestProspectLoginBindingResultModel() throws Exception {
//		 ProspectLogin prospectLogin = new ProspectLogin(TEST_EMAIL, "admin");
//		 Location location = new Location();
//		 location.setLocation_id(1);
//		 Applicant applicant = new Applicant();
//		 applicant.setEmail(TEST_EMAIL);
//		 applicant.setApplicant_id(243);
//		 applicant.setAddress("15 Mac Street");
//		 applicant.setDob("1989-06-01");		 
//		 applicant.setFirst_name("Sam");
//		 applicant.setGender("M");
//		 applicant.setHeight("46");
//		 applicant.setLast_name("Sharpe");
//		 applicant.setNationality("Canadian");
//		 applicant.setPhone_no("866-899-5522");
//		 applicant.setLocation_id(location);
//		 applicant.setProof_id(null);
//		 
//		 Password password = new Password();
//		 password.setApplicantId(applicant);
//		 password.setPassword(Utilities.getMd5("admin"));
//		 password.setPassword_id(334);
//		 List<Password> passlst = new ArrayList<Password>();
//		 passlst.add(password);
//
//		  when(applicantDb.findByEmail(TEST_EMAIL)).thenReturn(applicant);
//		  when(passwordDb.findByApplicantId(applicant)).thenReturn(passlst);
//		  
//		  this.mockMvc.perform(post("/drive_test")
//				  .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				  .param("email",TEST_EMAIL)
//					 .param("password", "admin")
//					 .sessionAttr("prospectLogin", prospectLogin)
//				  )
//		  	.andDo(print())
//			 .andExpect(status().is3xxRedirection())
//			 .andExpect(view().name("redirect:/main"))
//			 .andExpect(redirectedUrl("/main"));
//		  
//		  }
	 
	 
	
//	Testing the logout resquest
//	@Test
//	void testLogOut() throws Exception {
//		this.mockMvc.perform(get("/log-out"))
//	.andDo(print())
//	.andExpect(status().is3xxRedirection())
//	 .andExpect(view().name("redirect:/drive_test"))
//	 .andExpect(redirectedUrl("/drive_test"));
//	}

	
	//Testing the reset request
//	@Test
//	void testDriveTestReset() throws Exception{
//		Applicant applicant = new Applicant();
//		 applicant.setEmail(TEST_EMAIL);
//		 applicant.setApplicant_id(243);
//		 applicant.setAddress("15 Mac Street");
//		 applicant.setDob("1989-06-01");		 
//		 applicant.setFirst_name("Sam");
//		 applicant.setGender("M");
//		 applicant.setHeight("46");
//		 applicant.setLast_name("Sharpe");
//		 applicant.setNationality("Canadian");
//		 applicant.setPhone_no("866-899-5522");
//		 List<Applicant> allApplicant = new ArrayList<Applicant>();
//		 allApplicant.add(applicant);
//		 
//		 when(applicantDb.findAll()).thenReturn(allApplicant);
//		
//		this.mockMvc.perform(post("/reset-password")
//						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//						.param("email",TEST_EMAIL))
//						.andExpect(status().isOk())
//						.andExpect(content().string("data"))
//						.andDo(print());
//	}

//	@Test
//	void testDriveTestPwdChange() throws Exception{
//		this.mockMvc.perform(post("/change-password")
//				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
//				.param("auth_code","123")
//				.param("new_password","admin"))	
//				.andExpect(status().isOk())
//				.andExpect(content().string("data"))
//				.andDo(print());
//	}

//	Testing the get request for the registration page
//	@Test
//	void testDriveTestRegModel() throws Exception{
//		
//		Location location = new Location();
//		location.setLocation_id(1);	
//		location.setCity("Sarnia");
//		location.setProvince("ON");
//		location.setZip_code("N7S6H2");
//		
//		List<Location> allLocation = new ArrayList<Location>();
//		allLocation.add(location);
//		
//		 when(locationDb.findAll()).thenReturn(allLocation);
//		 System.out.println(new Prospect());
//		this.mockMvc.perform(get("/registration"))	
//				.andDo(print())
//				.andExpect(status().isOk())
////				.andExpect(model().attribute("prospect", new Prospect()))
//				.andExpect(view().name("driveTest/registration"));
//	}

//	Testing the post request for the registration page
//	@Test
//	void testDriveTestRegProspectBindingResultMultipartFileModel() {
//		fail("Not yet implemented");
//	}

	
//	Testing the get request for the main page
//	@Test
//	void testDriveTestMain() throws Exception{
	
//		modelSession.addAttribute("uid", 1);
//		modelSession.addAttribute("role", "Regular");
//		sessionattr.put("Active_User",  objectMapper.writeValueAsString(modelSession));
//		
//		this.mockMvc.perform(get("/main").sessionAttrs(sessionattr))	
//		.andDo(print())
//		.andExpect(status().isOk())
//		.andExpect(view().name("driveTest/main"));
//	}

//	Testing the get request for the report page
//	@Test
//	void testDriveTestReport() throws Exception{
//		this.mockMvc.perform(get("/dashboard/report"))	
//		.andDo(print())
//		.andExpect(status().isOk())
//		.andExpect(view().name("driveTest/report"));
//	}

//	Testing the get request for the quiz page
//	@Test
//	void testDriveTestQuiz() throws Exception{
//		modelSession.addAttribute("uid", 1);
//		modelSession.addAttribute("role", "Regular");
//		sessionattr.put("Active_User",  objectMapper.writeValueAsString(modelSession));
//		
//		this.mockMvc.perform(get("/quiz").sessionAttrs(sessionattr))	
//		.andDo(print())
//		 .andExpect(status().is3xxRedirection())
//		 .andExpect(view().name("redirect:/quiz/1"))
//		 .andExpect(redirectedUrl("/quiz/1"));
//	}

//	Testing the get request for the quiz page
//	@Test
//	void testDriveTestQuizNextModelInt() throws Exception{
//		
//		modelSession.addAttribute("uid", 1);
//		modelSession.addAttribute("role", "Regular");
//		sessionattr.put("Active_User",  objectMapper.writeValueAsString(modelSession));
//		List<Question> mcq = new ArrayList<Question>();
//		Question question = new Question();
//		question.setAnswer("D");
//		question.setQuestion("When arriving at an intersection which has no stop line, crosswalk or sidewalk, where must drivers stop?\n[\"Right beside the stop sign\",\"Right before the stop sign\",\"A little into the intersection\",\"At the edge of the intersection\"]");
//		question.setQuestionType("Image");
//		question.setMediaPath("/media/drivetest/images/image48.jpg");
//		question.setQues_id(311);
//		
//		mcq.add(question);
//		mcq.add(question);
//		mcq.add(question);
//		mcq.add(question);
//		mcq.add(question);
//		Integer five = 5;
//		 when(questionDb.count()).thenReturn(five.longValue());
//		 when(questionDb.findAll()).thenReturn(mcq);
//		this.mockMvc.perform(get("/quiz/1").sessionAttrs(sessionattr))	
//		.andDo(print())
//		 .andExpect(status().isOk())
//		 .andExpect(view().name("driveTest/quiz"));
//		
//	}

//	@Test
//	void testDriveTestQuizNextIntAnswerModel() {
//		fail("Not yet implemented");
//	} 
//
	@Test
	void testDriveTestAdminModel() throws Exception{
		for(int i=0; i<2; i++) {
			if (i==0) {
		this.mockMvc.perform(get("/admin"))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(model().attribute("prospectLogin", new ProspectLogin()))
			.andExpect(model().attribute("action", "/admin"))
			.andExpect(view().name("driveTest/login"));
		}
			else {
				
				modelSession.addAttribute("uid", 1);
				modelSession.addAttribute("role", "Regular");
				sessionattr.put("Active_User",  objectMapper.writeValueAsString(modelSession));
				
				this.mockMvc.perform(get("/admin").sessionAttrs(sessionattr))	
				.andDo(print())
				.andExpect(status().is3xxRedirection())
				 .andExpect(view().name("redirect:/dashboard"))
				 .andExpect(redirectedUrl("/dashboard"));
			}
		}
	}

//	@Test
//	void testDriveTestAdminProspectLogin() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetDashboard() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testQuestionSearch() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetDashboardqc() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testCategoryAssignmentSearch() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testAssignQCategories() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testNewCategory() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testNewGuardian() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testEditCategoryIntegerModel() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testEditCategoryIntegerCategoryBindingResultModelMap() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testNewQuestion() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testEditQuestionIntegerModel() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testEditQuestionIntegerQuestionBindingResultModelMap() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testNewProduct() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testNewProductW() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testEditProductIntegerModel() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testEditProductIntegerProductBindingResultMultipartFile() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetOrder() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testSetUpAdmin() {
//		fail("Not yet implemented");
//	}
	

//	@Test
//	void testSetAuthCodeSessionManager() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetAuthCodeSessionObject() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testSetUserSessionManager() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetUserSessionObject() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testSetSessionManager() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetSessionObject() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testSetSessionManagerAns() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testUpdateSessionManagerAns() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testGetSessionAnswerObject() {
//		fail("Not yet implemented");
//	}
//

}
