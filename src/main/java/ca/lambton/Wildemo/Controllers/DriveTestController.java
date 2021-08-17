package ca.lambton.Wildemo.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.lambton.Wildemo.AdditionalFunction.FileUploadUtil;
import ca.lambton.Wildemo.AdditionalFunction.ReCaptchaResponse;
import ca.lambton.Wildemo.AdditionalFunction.SmtpMailSender;
import ca.lambton.Wildemo.Models.BreadCrumbs;
import ca.lambton.Wildemo.Models.Utilities;
import ca.lambton.Wildemo.Models.WIL.Admin;
import ca.lambton.Wildemo.Models.WIL.AdminPassword;
import ca.lambton.Wildemo.Models.WIL.Answer;
import ca.lambton.Wildemo.Models.WIL.Applicant;
import ca.lambton.Wildemo.Models.WIL.Applicant_Quiz;
import ca.lambton.Wildemo.Models.WIL.Category;
import ca.lambton.Wildemo.Models.WIL.MultipleChoice;
import ca.lambton.Wildemo.Models.WIL.Password;
import ca.lambton.Wildemo.Models.WIL.Product;
import ca.lambton.Wildemo.Models.WIL.ProofIdentity;
import ca.lambton.Wildemo.Models.WIL.Prospect;
import ca.lambton.Wildemo.Models.WIL.ProspectLogin;
import ca.lambton.Wildemo.Models.WIL.Question;
import ca.lambton.Wildemo.Models.WIL.Question_Category;
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

@Controller
public class DriveTestController {

	@Autowired
	SmtpMailSender smtpMailSender;

	@Autowired
	private HttpSession session;

	@Autowired
	private ApplicantRepository applicantDb;

	@Autowired
	private AdminRepository adminDb;

	@Autowired
	private QuestionRepository questionDb;

	@Autowired
	private CategoryRepository categoryDb;

	@Autowired
	private LocationRepository locationDb;

	@Autowired
	private ProofIdentityRepository proofIdentityDb;

	@Autowired
	private QuestionCategoryRepository questionCategoryDb;

	@Autowired
	private PasswordRepository passwordDb;

	@Autowired
	private AdminPasswordRepository adminPasswordDb;

	@Autowired
	private ProductRepository productDb;

	@Autowired
	private ApplicantQuizRepository applicantQuizDb;

	@Autowired
	private BreadCrumbs breadCrumbs;
	
	
	
	/**
	 * 
	 */
	@Value("${recaptcha.secret}")
	private String recaptchaSecret;
	
	@Value("${recaptcha.url}")
	private String recaptchaURL;
	
	/**
	 * @param builder
	 * @return
	 */
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
		
	}
	
	/**
	 * 
	 */
	@Autowired
	private RestTemplate restTemplate;

	
	/**
	 * @param gRecapthcaResponse
	 * @return
	 */
	private boolean verifyReCAPTCHA(String gRecapthcaResponse) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("secret", recaptchaSecret);
		map.add("response", gRecapthcaResponse);
		
		HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(map,headers);
		ReCaptchaResponse response = restTemplate.postForObject(recaptchaURL,request,ReCaptchaResponse.class);
		
		System.out.println("SUCCESS:"+response.isSuccess());
		System.out.println("HOSTNAME:"+response.getHostname());
		System.out.println("CHALLENEGE TIMESTAMP:"+response.getChallenge_ts());
		
		if(response.getErrorCodes()!=null)
		{
			for(String error : response.getErrorCodes())
			{
				System.out.println("\t"+error);
			}
		}
		return response.isSuccess();
	}
	
	
	/**
	 * @param prospectLogin
	 * @param request
	 * @return
	 * @throws IOException
	 */
//	@PostMapping("/drive_test")
//	public String driveTest(ProspectLogin prospectLogin,HttpServletRequest request) throws IOException {
//		HttpServletResponse response = null;
//		
//		
//		Applicant applicant = applicantDb.findByEmail(prospectLogin.getEmail());
//		List<Password> lstpassword = passwordDb.findByApplicantId(applicant);
//		Password password = lstpassword.stream().sorted(Comparator.comparingInt(Password::getPassword_id).reversed())
//				.findFirst().get();
//		
//		
//		String gRecapthcaResponse = request.getParameter("g-recaptcha-response");
//		if(!verifyReCAPTCHA(gRecapthcaResponse))
//		{
//			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//		}
//		
//		
//		if (Utilities.getMd5(prospectLogin.getPassword()).equals(password.getPassword())) {
//			//return "redirect:/main";
//			return "redirect:/main";
//		}
//		
//		return "driveTest/login";
//	}
	
	
	

	@GetMapping("/")
	public String home() {
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				return "redirect:/admin";
			}
		}
		return "redirect:/drive_test";
	}
	
	// =========================================================================================================================
	// Login and Logout function
	// =========================================================================================================================
	// Entry point into the application
	@GetMapping("/drive_test")
	public String driveTest(Model model) {

		// checks if user is already login
		if (Utilities.getUserSessionObject(session) != null) {
			return "redirect:/main";
		}

		model.addAttribute("prospectLogin", new ProspectLogin());
		model.addAttribute("action", "/drive_test");
		return "driveTest/login";
	}

	
	//=========================================================================================================================
	// Deletes the active user session
	//=========================================================================================================================
	@GetMapping("/log-out")
	public String logOut() {

		// remove all session object (revamp code to use loop)
		session.removeAttribute("Active_User");
		session.removeAttribute("Quiz");

		return "redirect:/drive_test";
	}

	// =========================================================================================================================
	// Validate the user and login
	// =========================================================================================================================
	@PostMapping("/drive_test")
	public String driveTest(@Valid ProspectLogin prospectLogin, @RequestParam(name = "g-recaptcha-response") String gRecapthcaResponse, BindingResult bindingResult, Model model) throws IOException {
		
		HttpServletResponse response = null;
		//String gRecapthcaResponse = request.getParameter("g-recaptcha-response");
		if(!verifyReCAPTCHA(gRecapthcaResponse))
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		
		//)
//			throws MessagingException {

		// server-side validation
		if (bindingResult.hasErrors()) {
			return "driveTest/login";
		}

		// user authentication validation
		Applicant applicant = applicantDb.findByEmail(prospectLogin.getEmail());
		List<Password> lstpassword = passwordDb.findByApplicantId(applicant);
//		System.out.println(lstpassword);
		if (lstpassword.size() != 0) {
		Password password = lstpassword.stream().sorted(Comparator.comparingInt(Password::getPassword_id).reversed())
				.findFirst().orElseGet(null);

		
		
		// checks password encryption
		if (Utilities.getMd5(prospectLogin.getPassword()).equals(password.getPassword())) {
			setUserSessionManager(applicant.getApplicant_id(), "Regular");
			
			return "redirect:/main";
		}
		}

		// Error if password is incorrect
		model.addAttribute("loginErr", "Your username and password is invalid.");
		return "driveTest/login";
	}

	// =========================================================================================================================
	// End of login and logout function
	/** =========================================================================================================================**/

	
	
	// =========================================================================================================================
	// Method sends the authentication code to the user email
	// =========================================================================================================================
	@PostMapping("/reset-password")
	@ResponseBody
	public String driveTestReset(@RequestParam(name = "email") String emailStr, Model model) throws MessagingException {

		// checks if the email address exist in the db
		String existEmail = applicantDb.findAll().stream().map(x -> x.getEmail()).filter(x -> x.equals(emailStr))
				.findFirst().orElse(null);

		// if email exist send email with authentication code
		if (existEmail != null) {
			int result = new Random().nextInt(10);
			String code = "test" + result; // generates authentication code
			smtpMailSender.send(emailStr, "Password Reset", "Your authentication code is: " + code); // sends email
			setAuthCodeSessionManager(code, emailStr); // stores code in a session variable
			return "data";
		}
		return "no-data";

	}

	// =========================================================================================================================
	// Method changes user password
	// =========================================================================================================================
	@PostMapping("/change-password")
	@ResponseBody
	public String driveTestPwdChange(@RequestParam(name = "auth_code") String auth_code,
			@RequestParam(name = "new_password") String new_pwd) {

		ModelMap usrAuth = getAuthCodeSessionObject();
		if (usrAuth.getAttribute("AuthCode").toString().equals(auth_code)) {
			Applicant usr = applicantDb.findByEmail(usrAuth.getAttribute("Email").toString());
			Password pwd = new Password();
			pwd.setApplicantId(usr);
			pwd.setExpiryDate(Utilities.pwdExpiryDate());
			pwd.setPassword(Utilities.getMd5(new_pwd));

			passwordDb.save(pwd);
			return "data";
		}
		return "no-data";
	}

	// =========================================================================================================================
	// Method calls the registration form
	// =========================================================================================================================
	@GetMapping("/registration")
	public String driveTestReg(Model model) {
		
		// checks if user is already login
				if (Utilities.getUserSessionObject(session) != null) {
					return "redirect:/";
				}
				
		
		model.addAttribute("prospect", new Prospect());
		model.addAttribute("foreign", "foreign");
		ModelMap emptyMap = new ModelMap();
		emptyMap.addAttribute("Name", "---------");
		emptyMap.addAttribute("Num", "");

		// zip code data
		List<ModelMap> lstLocation = locationDb.findAll().stream().map(x -> {
			ModelMap std = new ModelMap();
			std.addAttribute("Name", x.toString());
			std.addAttribute("Num", x.getLocation_id());
			return std;
		}).collect(Collectors.toList());

		lstLocation.add(0, emptyMap);
		ModelMap foreignModel = new ModelMap();
		foreignModel.addAttribute("Zip", lstLocation);
		model.addAttribute("foreignModel", foreignModel);

		// Breadcrumbs
		breadCrumbs.start("Registration");
		model.addAttribute("links", breadCrumbs);

		return "driveTest/registration";
	}

	// =========================================================================================================================
	// Method registers an applicant
	// =========================================================================================================================
	@PostMapping("/registration")
	public String driveTestReg(@Valid Prospect prospect, BindingResult bindingResult,
			@RequestParam("file") MultipartFile file, Model model) {

		// server-side validation
		if (bindingResult.hasErrors()) {

			model.addAttribute("foreign", "foreign");
			ModelMap emptyMap = new ModelMap();
			emptyMap.addAttribute("Name", "---------");
			emptyMap.addAttribute("Num", "");

			// zip code data
			List<ModelMap> lstLocation = locationDb.findAll().stream().map(x -> {
				ModelMap std = new ModelMap();
				std.addAttribute("Name", x.toString());
				std.addAttribute("Num", x.getLocation_id());
				return std;
			}).collect(Collectors.toList());

			lstLocation.add(0, emptyMap);
			ModelMap foreignModel = new ModelMap();
			foreignModel.addAttribute("Zip", lstLocation);
			model.addAttribute("foreignModel", foreignModel);

			// Breadcrumbs
			breadCrumbs.start("Registration");
			model.addAttribute("links", breadCrumbs);

			return "driveTest/registration";
		}

		// Add proof to the database
		ProofIdentity prospect_proof = prospect.getProof();
		prospect_proof.setImage_file("---");
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		ProofIdentity proof = proofIdentityDb.save(prospect_proof);
		int pid = proof.getProof_id();

		String uploadDir = "resource-uploads/App_proof_images/" + pid;
		proof.setImage_file("/" + uploadDir + "/" + fileName);
		proofIdentityDb.save(proof);
		try {
			FileUploadUtil.saveFile(uploadDir, fileName, file);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// Add applicant to the database
		Applicant prospect_applicant = prospect.getApplicant();
		prospect_applicant.setProof_id(proof);
		Applicant applicant = applicantDb.save(prospect_applicant);

		// Add password to the database
		Password password = new Password();
		password.setApplicantId(applicant);
		password.setExpiryDate(Utilities.pwdExpiryDate());
		password.setPassword(Utilities.getMd5(prospect.getPassword()));

		passwordDb.save(password);

		return "redirect:/drive_test";
	}

	// =========================================================================================================================
	// The landing page after user has successfully log in. Method will redirect
	// user to login page if not properly logged in
	// =========================================================================================================================
	@GetMapping("/main")
	public String driveTestMain(Model model) {

		// redirects user if not logged in
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Regular")) {
				if (session.getAttribute("Quiz") != null) {
					return "redirect:/quiz";
				}
				// generates a last five quiz result for the logged in user
				List<Applicant_Quiz> appQuiz = applicantQuizDb.findAll().stream()
						.filter(x -> x.getApplicantId().getApplicant_id().equals(Utilities.getUserSessionObject(session).getAttribute("uid")))
						.sorted(Comparator.comparing(Applicant_Quiz::getQuizDate).reversed()).limit(5)
						.collect(Collectors.toList());
				model.addAttribute("attempts", appQuiz);

				boolean isSuccess = appQuiz.stream().map(x -> x.getResult()>=80).reduce(false, (val, elem)->val || elem);
				// Breadcrumbs
				breadCrumbs.start("Main");
				model.addAttribute("links", breadCrumbs);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				model.addAttribute("isSuccess", isSuccess);
				return "driveTest/main";
			}
			return "redirect:/";
		
		}
		return "redirect:/drive_test";

		
	}

	// =========================================================================================================================
	// Initialize the quiz application
	// =========================================================================================================================
	@GetMapping("/quiz")
	public String driveTestQuiz()// Model model)
	{
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Regular")) {
				setSessionManager();
				setSessionManagerAns();

				return "redirect:/quiz/1";
			}
			return "redirect:/";
		}
		return "redirect:/drive_test";
		
	}

	// =========================================================================================================================
	// Gets the next question in the list
	// =========================================================================================================================
	@GetMapping("/quiz/{id}")
	public String driveTestQuizNext(Model model, @PathVariable("id") int id) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Regular")) {
				if (questionDb.count() == 0) {
					return "redirect:/quiz";
				}
				setSessionManagerAns();
				List<String> ans = getSessionAnswerObject();

				setSessionManager();
				List<MultipleChoice> mm = getSessionObject();
				model.addAttribute("questionAns", ans.get(id - 1));
				model.addAttribute("ques", mm.get(id - 1));
				model.addAttribute("answerObj", new Answer());
				model.addAttribute("QUESTION_COUNT", Utilities.QUESTION_COUNT);
				model.addAttribute("questionNo", id);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				return "driveTest/quiz";
			}
			return "redirect:/";
		}
		return "redirect:/drive_test";
		
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@PostMapping("/quiz/{id}")
	public String driveTestQuizNext(@PathVariable("id") int id, Answer answer, Model model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Regular")) {
				setSessionManagerAns();
				List<String> ans = getSessionAnswerObject();
				ans.set(id - 1, answer.getAnswer());

				updateSessionManagerAns(ans);
				int i = id;
				if (getSessionObject() != null) {

					if (answer.getNext().equals("NEXT") && (Utilities.QUESTION_COUNT - 1) > (id - 1)) {
						i++;
					} else if (answer.getNext().equals("PREVIOUS") && (id - 1) > 0) {
						i--;
					} else if (answer.getNext().equals("SUBMIT QUIZ")) {
						List<MultipleChoice> mm = getSessionObject();
						// add the quiz result to database
						ModelMap activeUser = Utilities.getUserSessionObject(session);
						Integer uid = (Integer) activeUser.getAttribute("uid");
						double score = Utilities.quizScore(mm, ans);
						Applicant applicant = applicantDb.findById(uid).get();
						Applicant_Quiz appQuiz = new Applicant_Quiz();
						appQuiz.setApplicantId(applicant);
						appQuiz.setAnswersList(ans.toString());
						appQuiz.setQuizQuestionList(mm.stream().map(x -> x.getId()).collect(Collectors.toList()).toString());
						appQuiz.setResult(score);
						
						
						ModelMap modelmap = new ModelMap();
						modelmap.addAttribute("quizDate", null);
						modelmap.addAttribute("quizscore", score);

						

						applicantQuizDb.save(appQuiz);
						session.removeAttribute("Quiz");
						session.removeAttribute("Answer");
						
						//generate email with attachment only if successful
						if(score>=80) {
							
							try {
								// System.out.println("pdf");
								Utilities.ConvertToPDF(applicant.getFirst_name() + " " + applicant.getLast_name(), "" + score);
								
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}

							try {
								System.out.println("modify to get test taker email");
								smtpMailSender.send(applicant.getEmail(), "Quiz Results", "Please see the attached Quiz Results",
										"src/main/resources/Test_out.pdf");
							} catch (Exception e) {// MessagingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						
						return "redirect:/main";
					}

					model.addAttribute("questionAns", ans.get(i - 1));
					return "redirect:/quiz/" + i;
				}
				return "error";
			}
			return "redirect:/drive_test";
		}
		return "redirect:/drive_test";
	}

	

	// ===================================================================
	// Admin login
	// ===================================================================
	@GetMapping("/admin")
	public String driveTestAdmin(Model model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				return "redirect:/dashboard";
			}			
		}
		//else {
			model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
		//}
		model.addAttribute("prospectLogin", new ProspectLogin());
		model.addAttribute("action", "/admin");
		return "driveTest/login";
	}

	// =========================================================================================================================
	// Logs into the admin account
	// =========================================================================================================================
	@PostMapping("/admin")
	public String driveTestAdmin(@Valid ProspectLogin prospectLogin, BindingResult bindingResult, Model model) throws MessagingException {

		// server-side validation
		if (bindingResult.hasErrors()) {
			return "driveTest/login";
		}
		 
		Admin admin = adminDb.findByAdminEmail(prospectLogin.getEmail());
		List<AdminPassword> lstAdminPassword = adminPasswordDb.findByAdminId(admin);
		if (lstAdminPassword.size() != 0) {
		AdminPassword adminPassword = lstAdminPassword.stream()
				.sorted(Comparator.comparingInt(AdminPassword::getPassword_id).reversed()).findFirst().get();
		// System.out.println(applicant);
		if (Utilities.getMd5(prospectLogin.getPassword()).equals(adminPassword.getPassword())) {
			// add session for active user
			// System.out.println(applicant);
			setUserSessionManager(admin.getAdminId(), admin.getAdminRole());

//			isSetUser = admin.getAdminName();
			return "redirect:/dashboard";
		}
		}

		// Error if password is incorrect
		model.addAttribute("loginErr", "Your username and password is invalid.");
//		return "driveTest/login";
		return "driveTest/login";
	}
	
	
	
	// =========================================================================================================================
	// Shows the stats of user attempts and pass rate. Page is heavily coded in
	// JavaScript-Chart.JS
	// =========================================================================================================================
	@GetMapping("/dashboard/report")
	public String driveTestReport(Model model) {
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				breadCrumbs.start("Reports");
				model.addAttribute("links", breadCrumbs);
				return "driveTest/report";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
		
	}
	

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@GetMapping("/dashboard/questions")
	public String getDashboard(Model model) {
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				model.addAttribute("modIds", categoryDb.findAll());
				model.addAttribute("modelData", questionDb.findAll());
				model.addAttribute("modQid",
						questionDb.findAll().stream().map(x -> x.getQues_id()).collect(Collectors.toList()));

				//Breadcrumbs
				breadCrumbs.start("questions");
				model.addAttribute("links", breadCrumbs);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				return "driveTest/questiontable";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@GetMapping("/dashboard/questions/search")
	public String questionSearch(@RequestParam("question_id") String id, @RequestParam("category_id") String id2,
			Model model) {
		
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				List<Question> lq;
				List<Question_Category> lq1;
				if (id.equals("All") && !id2.equals("All")) {
					// filter by category
					lq1 = questionCategoryDb.findAll().stream()
							.filter(x -> (x.getCategoryId().getCat_id()) == Integer.parseInt(id2)).collect(Collectors.toList());
					lq = questionDb.findAll().stream().filter(x -> {
						boolean t = false;
						for (Question_Category c : lq1) {
							if (c.getQuestionId().getQues_id() == x.getQues_id())
								t = true;
						}
						return t;
					}).collect(Collectors.toList());
				} else if (!id.equals("All") && id2.equals("All")) {
					// filter by id
					lq = questionDb.findAll().stream().filter(x -> x.getQues_id() == Integer.parseInt(id))
							.collect(Collectors.toList());
				} else if (id.equals("All") && id2.equals("All")) {
					lq = questionDb.findAll();
				} else {
					// filter by category and id
					lq1 = questionCategoryDb.findAll().stream()
							.filter(x -> (x.getCategoryId().getCat_id()) == Integer.parseInt(id2)).collect(Collectors.toList());
					lq = questionDb.findAll().stream().filter(x -> {
						boolean t = false;
						for (Question_Category c : lq1) {
							if (c.getQuestionId().getQues_id() == x.getQues_id() && (Integer.parseInt(id2) == x.getQues_id()))
								t = true;
						}
						return t;
					}).collect(Collectors.toList());
				}

				model.addAttribute("modIds", categoryDb.findAll());
				model.addAttribute("modelData", lq);
				model.addAttribute("modQid",
						questionDb.findAll().stream().map(x -> x.getQues_id()).collect(Collectors.toList()));
				model.addAttribute("filterQ", id);
				model.addAttribute("filterC", id2);
				
				//Breadcrumbs
				breadCrumbs.start("questions");
				model.addAttribute("links", breadCrumbs);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				return "driveTest/questiontable";
			}
			return "redirect:/";
		}
		return "redirect:/admin";	
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@GetMapping("/dashboard/question-categories")
	public String getDashboardqc(Model model) {
		
		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				model.addAttribute("modIds", categoryDb.findAll());
				model.addAttribute("modelData", questionDb.findAll());
				model.addAttribute("disabled", false);
				
				//Breadcrumbs
				breadCrumbs.start("question-categories");
				model.addAttribute("links", breadCrumbs);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				return "driveTest/qcassignment";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@GetMapping("/dashboard/{modelName}/qcsearch/")
	public String categoryAssignmentSearch(@PathVariable("modelName") String modelName,
			@RequestParam("category_id") String id, @RequestParam("action_id") String id2, Model model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				List<Question_Category> lq1;
				if (id.equals("All")) {
					lq1 = questionCategoryDb.findAll();
				} else {
					lq1 = questionCategoryDb.findAll().stream()
							.filter(x -> (x.getCategoryId().getCat_id()) == Integer.parseInt(id)).collect(Collectors.toList());
				}

				List<Question> lq = questionDb.findAll().stream().filter(x -> {

					boolean t = true;
					if (id2.equals("Assigned")) {
						t = false;
						for (Question_Category c : lq1) {
							t = t || (c.getQuestionId().getQues_id() == x.getQues_id());
						}
					} else if (id2.equals("Unassigned")) {
						t = true;
						for (Question_Category c : lq1) {
							t = t && (c.getQuestionId().getQues_id() != x.getQues_id());
						}
					}

					return t;
				}).collect(Collectors.toList());

				model.addAttribute("modIds", categoryDb.findAll());
				model.addAttribute("modelData", lq);
				model.addAttribute("filter_category", id);
				model.addAttribute("filter_action", id2);
				model.addAttribute("disabled", true);
				
				//Breadcrumbs
				breadCrumbs.start("question-categories");
				model.addAttribute("links", breadCrumbs);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				return "driveTest/qcassignment";
			}
			return "redirect:/";
		}
		return "redirect:/admin";	
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@PostMapping("/dashboard/question-categories")
	@ResponseBody
	public String assignQCategories(@RequestParam(name = "questions") String question,
			@RequestParam(name = "category_id") String category, @RequestParam(name = "action_id") String action) {
		
		if (Utilities.getUserSessionObject(session) == null) {
			return "redirect:/admin";
		}

		// create a list of contributors from the json file
		ObjectMapper objectMapper = new ObjectMapper();
		List<Integer> options = null;
		try {
			options = objectMapper.readValue(question, new TypeReference<List<Integer>>() {
			});
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(options);
		System.out.println(category);
		System.out.println(action);
		if (action.equals("Assign")) {
			for (Integer i : options) {
				Question_Category qc = new Question_Category();
				qc.setCategoryId(categoryDb.findById(Integer.parseInt(category)).orElse(null));
				qc.setQuestionId(questionDb.findById(i).orElse(null));
				questionCategoryDb.save(qc);
			}
		} else if (action.equals("Unassign")) {
			for (Integer i : options) {
				Question_Category qc = questionCategoryDb.findAll().stream()
						.filter(x -> x.getCategoryId().getCat_id() == Integer.parseInt(category))
						.filter(y -> y.getQuestionId().getQues_id() == i).findFirst().orElse(null);
				questionCategoryDb.delete(qc);
			}
		}

		return "assigned";
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@GetMapping("/dashboard/categories/add-new")
	public String newCategory(Model model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				model.addAttribute("modelClass", new Category());
				model.addAttribute("destination", "/dashboard/categories/add-new/");
				// model.addAttribute("encoding", "multipart/form-data");
				model.addAttribute("method", "post");

				breadCrumbs.start("categories_add-new");
				model.addAttribute("links", breadCrumbs);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				return "driveTest/form";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@PostMapping("/dashboard/categories/add-new")
	public String newGuardian(@Valid @ModelAttribute("modelClass") Category category, BindingResult bindingResult,
			ModelMap model) {
		
		if (Utilities.getUserSessionObject(session) == null) {
			return "redirect:/admin";
		}

		if (bindingResult.hasErrors()) {
			return "layouts/form_components/main_form";
		}
		categoryDb.save(category);

		return "redirect:/dashboard/categories";
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@GetMapping("/dashboard/categories/edit/{id}")
	public String editCategory(@PathVariable("id") Integer id, Model model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				if (categoryDb.findById(id).isPresent()) {
					Category category = categoryDb.findById(id).get();
					model.addAttribute("modelClass", category);
					model.addAttribute("destination", "/dashboard/categories/edit/" + id);
					model.addAttribute("prod_status", "edit");
					// model.addAttribute("encoded", "multipart/form-data");
					model.addAttribute("method", "post");

					breadCrumbs.start("categories_edit");
					model.addAttribute("links", breadCrumbs);
					model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
					return "driveTest/form";
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@PostMapping("/dashboard/categories/edit/{id}")
	public String editCategory(@PathVariable("id") Integer id, @Valid @ModelAttribute("modelClass") Category category,
			BindingResult bindingResult, ModelMap model) {

		if (Utilities.getUserSessionObject(session) == null) {
			return "redirect:/admin";
		}
		
		if (bindingResult.hasErrors()) {
			return "driveTest/form";
		}
		if (categoryDb.findById(id).isPresent()) {
			Category categoryUpdate = categoryDb.findById(id).get();
			categoryUpdate.setDescription(category.getDescription());
			categoryUpdate.setName(category.getName());
			categoryDb.save(categoryUpdate);
			return "redirect:/dashboard/categories";
		}
		return "error";
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@GetMapping("/dashboard/questions/add-new")
	public String newQuestion(Model model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				model.addAttribute("modelClass", new Question());
				model.addAttribute("destination", "/dashboard/categories/add-new/");
				// model.addAttribute("encoding", "multipart/form-data");
				model.addAttribute("method", "post");

				breadCrumbs.start("questions_add-new");
				model.addAttribute("links", breadCrumbs);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				return "driveTest/form";
			}
			return "redirect:/";
		}
		return "redirect:/admin";	
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@GetMapping("/dashboard/questions/edit/{id}")
	public String editQuestion(@PathVariable("id") Integer id, Model model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				if (questionDb.findById(id).isPresent()) {
					Question question = questionDb.findById(id).get();
					model.addAttribute("modelClass", question);
					model.addAttribute("destination", "/dashboard/questions/edit/" + id);
					model.addAttribute("prod_status", "edit");
					// model.addAttribute("encoded", "multipart/form-data");
					model.addAttribute("method", "post");

					breadCrumbs.start("questions_edit");
					model.addAttribute("links", breadCrumbs);
					model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
					return "driveTest/form";
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";	
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@PostMapping("/dashboard/questions/edit/{id}")
	public String editQuestion(@PathVariable("id") Integer id, @Valid @ModelAttribute("modelClass") Question question,
			BindingResult bindingResult, ModelMap model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				if (bindingResult.hasErrors()) {
					return "driveTest/form";
				}
				if (questionDb.findById(id).isPresent()) {
					Question questionUpdate = questionDb.findById(id).get();
					questionUpdate.setAnswer(question.getAnswer());
					questionUpdate.setMediaType(question.getMediaType());
					questionUpdate.setQuestion(question.getQuestion());
					questionUpdate.setQuestionType(question.getQuestionType());
					questionUpdate.setMediaPath(question.getMediaPath());
					questionDb.save(questionUpdate);
					return "redirect:/dashboard/questions";
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@GetMapping("/dashboard/products/add-new")
	public String newProduct(Model model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				model.addAttribute("modelClass", new Product());
				model.addAttribute("destination", "/dashboard/products/add-new/");
				model.addAttribute("encoding", "multipart/form-data");
				model.addAttribute("method", "post");

				breadCrumbs.start("Products_add-new");
				model.addAttribute("links", breadCrumbs);
				model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
				return "driveTest/form";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
		
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@PostMapping("/dashboard/products/add-new")
	public String newProductW(@Valid @ModelAttribute("modelClass") Product product, BindingResult bindingResult,
			@RequestParam("file") MultipartFile file) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				if (bindingResult.hasErrors()) {
					return "driveTest/form";
				}

				String fileName = StringUtils.cleanPath(file.getOriginalFilename());
				Product pd = productDb.save(product);
				int pid = pd.getProductId();

				String uploadDir = "resource-uploads/product-images/" + pid;
				pd.setImage_file("/" + uploadDir + "/" + fileName);
				productDb.save(pd);
				try {
					FileUploadUtil.saveFile(uploadDir, fileName, file);
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}

				return "redirect:/dashboard/products";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@GetMapping("/dashboard/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, Model model) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				if (productDb.findById(id).isPresent()) {
					Product product = productDb.findById(id).get();
					model.addAttribute("modelClass", product);
					model.addAttribute("destination", "/dashboard/products/edit/" + id);
					model.addAttribute("prod_status", "edit");
					model.addAttribute("encoding", "multipart/form-data");
					model.addAttribute("method", "post");

					breadCrumbs.start("Products_edit");
					model.addAttribute("links", breadCrumbs);
					model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
					return "driveTest/form";
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	@PostMapping("/dashboard/products/edit/{id}")
	public String editProduct(@PathVariable("id") Integer id, @Valid Product product, BindingResult bindingResult,
			@RequestParam("file") MultipartFile file) {

		if (Utilities.getUserSessionObject(session) != null) {
			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Admin")) {
				if (bindingResult.hasErrors()) {
					return "driveTest/form";
				}
				if (productDb.findById(id).isPresent()) {
					Product productToUpdate = productDb.findById(id).get();
					productToUpdate.setName(product.getName());
					productToUpdate.setPrice(product.getPrice());
					productToUpdate.setDescription(product.getDescription());

					if (!file.isEmpty()) {
						try {
							String fileName = StringUtils.cleanPath(file.getOriginalFilename());
							String uploadDir = "resource-uploads/product-images/" + productToUpdate.getProductId();
							productToUpdate.setImage_file("/" + uploadDir + "/" + fileName);
							FileUploadUtil.saveFile(uploadDir, fileName, file);
						} catch (IOException e) {
							System.out.println("The product image wasn't uploaded.");
						}
					}
					productDb.save(productToUpdate);
					return "redirect:/dashboard/products";
				}
				return "error";
			}
			return "redirect:/";
		}
		return "redirect:/admin";	
	}

	// =========================================================================================================================
	// Calls the store page
	// =========================================================================================================================
	@GetMapping("/store")
	public String shoreStore(Model model) {
		List<Product> storeproducts = productDb.findAll();
	
		model.addAttribute("storeproducts", storeproducts);
		model.addAttribute("modelData", null);
	
		//Breadcrumbs
		breadCrumbs.start("Store");
		model.addAttribute("links", breadCrumbs);
		model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
		return "driveTest/store";
	}
	

	
	
	
	// =========================================================================================================================
	// Calls the checkout page
	// =========================================================================================================================
	@GetMapping("/store/checkout/")
	public String getOrder(@RequestParam("product") String id, Model model) {

		// create a list of contributors from the json file
		ObjectMapper objectMapper = new ObjectMapper();
		List<ModelMap> options = null;
		try {
			options = objectMapper.readValue(id, new TypeReference<List<ModelMap>>() {
			});
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		double totalCost = 0;
		for (ModelMap m : options) {
			Integer j = (Integer) m.get("value");
			totalCost += j.doubleValue();
		}
		String clientID = "Link from the application.properties";
		model.addAttribute("model", options);
		model.addAttribute("count", options.size());
		model.addAttribute("totalcost", totalCost);
		model.addAttribute("clientID", clientID);

		//Breadcrumbs
		breadCrumbs.start("Checkout");
		model.addAttribute("links", breadCrumbs);
		model.addAttribute("isSetUser", Utilities.isSetUser(session, applicantDb, adminDb));
		return "driveTest/order";
	}

	// =========================================================================================================================
	// Sets up a default admin account
	// =========================================================================================================================
	public void setUpAdmin() {
		final String ADMIN_ID = "admin@admin.com";
		final String ADMIN_PWD = "admin";
		if (adminDb.findAll().stream().filter(x -> x.getAdminRole().equals("Super User")).count() == 0) {
			Admin admin = new Admin();
			admin.setAdminName("Super User");
			admin.setAdminRole("Super User");
			admin.setAdminEmail(ADMIN_ID);
			admin.setAdminStatus("active");
			adminDb.save(admin);

			AdminPassword adminPwd = new AdminPassword();
			adminPwd.setAdminId(adminDb.findByAdminEmail(admin.getAdminEmail()));
			adminPwd.setPassword(Utilities.getMd5(ADMIN_PWD));
			adminPwd.setExpiryDate(Utilities.pwdExpiryDate());
			adminPasswordDb.save(adminPwd);
		}
	}

	// =========================================================================================================================
		//
		// =========================================================================================================================
		public void updateSessionManagerAns(List<String> ans) {
			// HttpSession session = request.getSession();
			ObjectMapper objectMapper = new ObjectMapper();
			String quizJson = "";
//			if (session.getAttribute("Answer") == null) {
			try {
				quizJson = objectMapper.writeValueAsString(ans);
			} catch (Exception e) {
				System.out.println();
			}

			session.setAttribute("Answer", quizJson);
//			}
		}

		// =========================================================================================================================
		//
		// =========================================================================================================================
		public List<String> getSessionAnswerObject() {
			List<String> mcq = new ArrayList<String>();
			if (session.getAttribute("Answer") != null) {
				ObjectMapper objectMapper = new ObjectMapper();
				String p = session.getAttribute("Answer").toString();
				try {
					mcq = objectMapper.readValue(p, new TypeReference<List<String>>() {
					});
				} catch (Exception e) {
					System.out.println(e);
				}
			}

			return mcq;
		}
	
	// =========================================================================================================================
	//
	// =========================================================================================================================
	// login reset session manager
	public void setAuthCodeSessionManager(String code, String email) {
		// HttpSession session = request.getSession();
		ObjectMapper objectMapper = new ObjectMapper();
		String quizJson = "";
		if (session.getAttribute("Reset_Code") == null) {
			try {
				ModelMap sessionModel = new ModelMap();
				sessionModel.addAttribute("AuthCode", code);
				sessionModel.addAttribute("Email", email);
//						sessionModel.addAttribute("role", role);
				quizJson = objectMapper.writeValueAsString(sessionModel);
			} catch (Exception e) {
				System.out.println();
			}

			session.setAttribute("Reset_Code", quizJson);
		}
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	public ModelMap getAuthCodeSessionObject() {
		ModelMap activeUser = null;
		if (session.getAttribute("Reset_Code") != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			String p = session.getAttribute("Reset_Code").toString();
			try {
				activeUser = objectMapper.readValue(p, new TypeReference<ModelMap>() {
				});
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return activeUser;
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	// login session manager
	public void setUserSessionManager(Integer uid, String role) {
		// HttpSession session = request.getSession();
		ObjectMapper objectMapper = new ObjectMapper();
		String quizJson = "";
		if (session.getAttribute("Active_User") == null) {
			try {
				ModelMap sessionModel = new ModelMap();
				sessionModel.addAttribute("uid", uid);
				sessionModel.addAttribute("role", role);
				quizJson = objectMapper.writeValueAsString(sessionModel);
			} catch (Exception e) {
				System.out.println();
			}

			session.setAttribute("Active_User", quizJson);
		}
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
//	public ModelMap getUserSessionObject(HttpSession session) {
//		ModelMap activeUser = null;
//		if (session.getAttribute("Active_User") != null) {
//			ObjectMapper objectMapper = new ObjectMapper();
//			String p = session.getAttribute("Active_User").toString();
//			try {
//				activeUser = objectMapper.readValue(p, new TypeReference<ModelMap>() {
//				});
//			} catch (Exception e) {
//				System.out.println(e);
//			}
//		}
//
//		return activeUser;
//	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	public void setSessionManager() {
		// HttpSession session = request.getSession();
		ObjectMapper objectMapper = new ObjectMapper();
		String quizJson = "";
		if (session.getAttribute("Quiz") == null) {
			try {
				quizJson = objectMapper.writeValueAsString(Utilities.getMCQQuestions(questionDb.findAll()));
			} catch (Exception e) {
				System.out.println();
			}

			session.setAttribute("Quiz", quizJson);
		}
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	public List<MultipleChoice> getSessionObject() {
		List<MultipleChoice> mcq = new ArrayList<MultipleChoice>();
		if (session.getAttribute("Quiz") != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			String p = session.getAttribute("Quiz").toString();
			try {
				mcq = objectMapper.readValue(p, new TypeReference<List<MultipleChoice>>() {
				});
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return mcq;
	}

	// =========================================================================================================================
	//
	// =========================================================================================================================
	// method handles the session variable that stores each questions answer
	public void setSessionManagerAns() {
		// HttpSession session = request.getSession();
		ObjectMapper objectMapper = new ObjectMapper();
		String quizJson = "";
		if (session.getAttribute("Answer") == null) {
			try {
				quizJson = objectMapper.writeValueAsString(new String[Utilities.QUESTION_COUNT]);
			} catch (Exception e) {
				System.out.println();
			}

			session.setAttribute("Answer", quizJson);
		}
	}
	
//	public String Utilities.isSetUser(session, applicantDb, adminDb) {
//		if (Utilities.getUserSessionObject(session) != null) {
//			if (Utilities.getUserSessionObject(session).getAttribute("role").equals("Regular"))
//				return  applicantDb.findById((Integer) Utilities.getUserSessionObject(session).getAttribute("uid")).get().getFirst_name();
//			else
//				return  adminDb.findById((Integer) Utilities.getUserSessionObject(session).getAttribute("uid")).get().getAdminName();
//		}
//		return null;
//	}
	
	
	
	

}
