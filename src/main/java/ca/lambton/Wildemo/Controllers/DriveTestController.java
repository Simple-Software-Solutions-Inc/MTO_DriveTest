package ca.lambton.Wildemo.Controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.lambton.Wildemo.AdditionalFunction.FileUploadUtil;
import ca.lambton.Wildemo.AdditionalFunction.SmtpMailSender;
import ca.lambton.Wildemo.Models.Utilities;
import ca.lambton.Wildemo.Models.WIL.Answer;
import ca.lambton.Wildemo.Models.WIL.Applicant;
import ca.lambton.Wildemo.Models.WIL.Applicant_Quiz;
import ca.lambton.Wildemo.Models.WIL.MultipleChoice;
import ca.lambton.Wildemo.Models.WIL.Password;
import ca.lambton.Wildemo.Models.WIL.ProofIdentity;
import ca.lambton.Wildemo.Models.WIL.Prospect;
import ca.lambton.Wildemo.Models.WIL.ProspectLogin;
import ca.lambton.Wildemo.Models.WIL.Question;
import ca.lambton.Wildemo.Models.WIL.Question_Category;
import ca.lambton.Wildemo.Repositories.WIL.ApplicantQuizRepository;
import ca.lambton.Wildemo.Repositories.WIL.ApplicantRepository;
import ca.lambton.Wildemo.Repositories.WIL.CategoryRepository;
import ca.lambton.Wildemo.Repositories.WIL.LocationRepository;
import ca.lambton.Wildemo.Repositories.WIL.PasswordRepository;
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
	private QuestionRepository questionDb;
	
	@Autowired
	private CategoryRepository categoryDb;

	@Autowired
	private LocationRepository locationDb;

	@Autowired
	private ProofIdentityRepository proofIdentityDb;

	@Autowired
	private PasswordRepository passwordDb;

	@Autowired
	private ApplicantQuizRepository applicantQuizDb;

	@GetMapping("/drive_test")
	public String driveTest(Model model) {

		if (getUserSessionObject() != null) {
			return "redirect:/main";
		}
		model.addAttribute("prospectLogin", new ProspectLogin());
		model.addAttribute("action", "/drive_test");
		return "driveTest/login";
	}

	// Deletes the active user session
	@GetMapping("/log-out")
	public String logOut() {

		session.removeAttribute("Active_User");
		return "redirect:/drive_test";
	}

	@PostMapping("/drive_test")
	public String driveTest(ProspectLogin prospectLogin) throws MessagingException {
		
		Applicant applicant = applicantDb.findByEmail(prospectLogin.getEmail());
		List<Password> lstpassword = passwordDb.findByApplicantId(applicant);
		Password password = lstpassword.stream().sorted(Comparator.comparingInt(Password::getPassword_id).reversed())
				.findFirst().get();
		// System.out.println(applicant);
		if (Utilities.getMd5(prospectLogin.getPassword()).equals(password.getPassword())) {
			// add session for active user
			// System.out.println(applicant);
			setUserSessionManager(applicant.getApplicant_id(), "Regular");

			return "redirect:/main";
		}
		return "driveTest/login";
	}

	// Method sends the authentication code to the user email
	@PostMapping("/reset-password")
	@ResponseBody
	public String driveTestReset(@RequestParam(name = "email") String str) throws MessagingException {

		int result = new Random().nextInt(10);
		String code = "test" + result; // generates authentication code
		smtpMailSender.send(str, "Test Mail", "Authentication code " + code); // sends email
		setAuthCodeSessionManager(code, str); // stores code in a session variable
		return "data";
	}

	// Method changes user password
	@PostMapping("/change-password")
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
			return "redirect:/drive_test";
		}
		return "redirect:/drive_test";
	}

	// Method calls the registration form
	@GetMapping("/registration")
	public String driveTestReg(Model model) {
		model.addAttribute("prospect", new Prospect());
		model.addAttribute("foreign", "foreign");
		ModelMap emptyMap = new ModelMap();
		emptyMap.addAttribute("Name", "---------");
		emptyMap.addAttribute("Num", "");

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

		return "driveTest/registration";
	}

	// Method registers an applicant
	@PostMapping("/registration")
	public String driveTestReg(Prospect prospect, @RequestParam("file") MultipartFile file) {

		// System.out.println(prospect.getPassword());

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

	@GetMapping("/main")
	public String driveTestMain(Model model) {
		if (getUserSessionObject() == null) {
			return "redirect:/drive_test";
		}
		// System.out.println(getUserSessionObject());
		List<Applicant_Quiz> appQuiz = applicantQuizDb.findAll().stream()
				.filter(x -> x.getApplicantId().getApplicant_id().equals(getUserSessionObject().getAttribute("uid")))
				.sorted(Comparator.comparing(Applicant_Quiz::getQuizDate).reversed()).limit(5)
				.collect(Collectors.toList());
		model.addAttribute("attempts", appQuiz);
		// System.out.println(appQuiz.size());
		// System.out.println((Integer) getUserSessionObject().getAttribute("uid"));
		return "driveTest/main";
	}

	@GetMapping("/report")
	public String driveTestReport() {
		return "driveTest/report";
	}

	@GetMapping("/certificate")
	public String driveTestCert() {
		return "driveTest/certificate";
	}

	@GetMapping("/quiz")
	public String driveTestQuiz()// Model model)
	{
		if (getUserSessionObject() == null) {
			return "redirect:/drive_test";
		}
		if (questionDb.count() == 0) {
			for (Question m : Utilities.loadQuestions()) {
				questionDb.save(m);
			}
		}
		setSessionManager();
		setSessionManagerAns();
//		List<MultipleChoice> mm = getSessionObject();
//		model.addAttribute("question", mm);

		return "redirect:/quiz/1";
	}

	@GetMapping("/quiz/{id}")
	public String driveTestQuizNext(Model model, @PathVariable("id") int id) {

		if (getUserSessionObject() == null) {
			return "redirect:/drive_test";
		}

		if (questionDb.count() == 0) {
			for (Question m : Utilities.loadQuestions()) {
				questionDb.save(m);
			}
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
		return "driveTest/quiz";
	}

	@PostMapping("/quiz/{id}")
	public String driveTestQuizNext(@PathVariable("id") int id, Answer answer, Model model) {

		if (getUserSessionObject() == null) {
			return "redirect:/drive_test";
		}
		setSessionManagerAns();
		List<String> ans = getSessionAnswerObject();
//		ans.add(id-1, answer.getAnswer());
		ans.set(id - 1, answer.getAnswer());
		// System.out.println("Answer:");
		// ans.forEach(System.out::println);
//		ans.set(id-1, answer.getAnswer());
		updateSessionManagerAns(ans);
		int i = id;
		if (getSessionObject() != null) {

			if (answer.getNext().equals("NEXT") && (Utilities.QUESTION_COUNT - 1) > (id - 1)) {
				i++;
			} else if (answer.getNext().equals("PREVIOUS") && (id - 1) > 0) {
				i--;
			} else if (answer.getNext().equals("SUBMIT QUIZ")) {
				try {
					// Utilities.generatePDFFromHTML("src/main/resources/templates/driveTest/certificate.html");
//					Utilities.ConvertToPDF("John Brown", "34");
					System.out.println("pdf");
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

				try {
					System.out.println("email");
//					smtpMailSender.send("example@example.com", "Test Mail", "Test Results", "src/main/resources/Test_out.pdf");
				} catch (Exception e) {// MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				List<MultipleChoice> mm = getSessionObject();

				ModelMap modelmap = new ModelMap();
				modelmap.addAttribute("quizDate", null);
				modelmap.addAttribute("quizscore", Utilities.quizScore(mm, ans));

				// add the quiz result to database
				ModelMap activeUser = getUserSessionObject();
				Integer uid = (Integer) activeUser.getAttribute("uid");
				Applicant_Quiz appQuiz = new Applicant_Quiz();
				appQuiz.setApplicantId(applicantDb.findById(uid).get());
				appQuiz.setAnswersList(ans.toString());
				appQuiz.setQuizQuestionList(mm.stream().map(x -> x.getId()).collect(Collectors.toList()).toString());
				appQuiz.setResult(Utilities.quizScore(mm, ans));

				applicantQuizDb.save(appQuiz);
				session.removeAttribute("Quiz");
				session.removeAttribute("Answer");
				return "redirect:/main";
			}

			model.addAttribute("questionAns", ans.get(i - 1));
			return "redirect:/quiz/" + i;
		}
		return "error";

	}

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
//					sessionModel.addAttribute("role", role);
				quizJson = objectMapper.writeValueAsString(sessionModel);
			} catch (Exception e) {
				System.out.println();
			}

			session.setAttribute("Reset_Code", quizJson);
		}
	}

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

//=================================================================================	

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

	public ModelMap getUserSessionObject() {
		ModelMap activeUser = null;
		if (session.getAttribute("Active_User") != null) {
			ObjectMapper objectMapper = new ObjectMapper();
			String p = session.getAttribute("Active_User").toString();
			try {
				activeUser = objectMapper.readValue(p, new TypeReference<ModelMap>() {
				});
			} catch (Exception e) {
				System.out.println(e);
			}
		}

		return activeUser;
	}

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

	public void updateSessionManagerAns(List<String> ans) {
		// HttpSession session = request.getSession();
		ObjectMapper objectMapper = new ObjectMapper();
		String quizJson = "";
//		if (session.getAttribute("Answer") == null) {
		try {
			quizJson = objectMapper.writeValueAsString(ans);
		} catch (Exception e) {
			System.out.println();
		}

		session.setAttribute("Answer", quizJson);
//		}
	}

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
	
	//===================================================================
	// Admin dashboard
	
	@GetMapping("/admin")
	public String driveTestAdmin(Model model) {

		if (getUserSessionObject() != null) {
			return "redirect:/dashboard";
		}
		model.addAttribute("prospectLogin", new ProspectLogin());
		model.addAttribute("action", "/admin");
		return "driveTest/login";
	}
	
	
	@PostMapping("/admin")
	public String driveTestAdmin(ProspectLogin prospectLogin) throws MessagingException {
		
		Applicant applicant = applicantDb.findByEmail(prospectLogin.getEmail());
		List<Password> lstpassword = passwordDb.findByApplicantId(applicant);
		Password password = lstpassword.stream().sorted(Comparator.comparingInt(Password::getPassword_id).reversed())
				.findFirst().get();
		// System.out.println(applicant);
		if (Utilities.getMd5(prospectLogin.getPassword()).equals(password.getPassword())) {
			// add session for active user
			// System.out.println(applicant);
			setUserSessionManager(applicant.getApplicant_id(), "Regular");

			return "redirect:/dashboard";
		}
		return "driveTest/login";
	}
	
	
	@Autowired
	private QuestionCategoryRepository questionCategoryDb;
	
	@GetMapping("/app/questions")
	public String getDashboard(Model model) {

		model.addAttribute("modIds", categoryDb.findAll());
		model.addAttribute("modelData", questionDb.findAll());
		return "driveTest/admin";
		
		
		
		
		
	}

}
