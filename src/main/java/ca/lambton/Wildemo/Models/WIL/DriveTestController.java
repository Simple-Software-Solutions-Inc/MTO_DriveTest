package ca.lambton.Wildemo.Models.WIL;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.lambton.Wildemo.AdditionalFunction.FileUploadUtil;
import ca.lambton.Wildemo.Models.Utilities;
import ca.lambton.Wildemo.Repositories.WIL.ApplicantRepository;
import ca.lambton.Wildemo.Repositories.WIL.LocationRepository;
import ca.lambton.Wildemo.Repositories.WIL.PasswordRepository;
import ca.lambton.Wildemo.Repositories.WIL.ProofIdentityRepository;
import ca.lambton.Wildemo.Repositories.WIL.QuestionRepository;

@Controller
public class DriveTestController {

	@Autowired
	private HttpSession session;
	
	@Autowired
	private ApplicantRepository applicantDb;

	@Autowired
	private QuestionRepository questionDb;

	@Autowired
	private LocationRepository locationDb;

	@Autowired
	private ProofIdentityRepository proofIdentityDb;

	@Autowired
	private PasswordRepository passwordDb;

	@GetMapping("/drive_test")
	public String driveTest(Model model) {

		model.addAttribute("prospectLogin", new ProspectLogin());
		return "driveTest/login";
	}

	@PostMapping("/drive_test")
	public String driveTest(ProspectLogin prospectLogin) {

		Applicant applicant = applicantDb.findByEmail(prospectLogin.getEmail());
		List<Password> lstpassword = passwordDb.findByApplicantId(applicant);
		Password password = lstpassword.stream().sorted(Comparator.comparingInt(Password::getPassword_id).reversed())
				.findFirst().get();

		if (Utilities.getMd5(prospectLogin.getPassword()).equals(password.getPassword())) {
			return "redirect:/main";
		}
		return "driveTest/login";
	}

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

	@PostMapping("/registration")
	public String driveTestReg(Prospect prospect, @RequestParam("file") MultipartFile file) {

		System.out.println(prospect.getPassword());

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
	public String driveTestMain() {
		return "driveTest/main";
	}

	@GetMapping("/quiz")
	public String driveTestQuiz()//Model model) 
	{

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

		if (questionDb.count() == 0) {
			for (Question m : Utilities.loadQuestions()) {
				questionDb.save(m);
			}
		}
		setSessionManagerAns();
		List<String> ans = getSessionAnswerObject();
		
		
		setSessionManager();
		List<MultipleChoice> mm = getSessionObject();
		model.addAttribute("questionAns", ans.get(id-1));
		model.addAttribute("ques", mm.get(id-1));
		model.addAttribute("answerObj",new Answer());
		return "driveTest/quiz";
	}
	
	
	@PostMapping("/quiz/{id}")
	public String driveTestQuizNext(@PathVariable("id") int id, Answer answer, Model model) {

		setSessionManagerAns();
		List<String> ans = getSessionAnswerObject();
//		ans.add(id-1, answer.getAnswer());
		ans.set(id-1, answer.getAnswer());
		System.out.println("Answer:");
		ans.forEach(System.out::println);
//		ans.set(id-1, answer.getAnswer());
		updateSessionManagerAns(ans);
		int i = id;
		if (getSessionObject() != null) {

			
			if (answer.getNext().equals("NEXT") && 4 > (id-1)) {
				i++;
			} else if (answer.getNext().equals("PREVIOUS") && (id-1) > 0) {
				i--;
			}else if (answer.getNext().equals("SUBMIT QUIZ")) {
				List<MultipleChoice> mm = getSessionObject();
				
				ModelMap modelmap = new ModelMap();
				modelmap.addAttribute("quizDate", null);
				modelmap.addAttribute("quizscore", Utilities.quizScore( mm, ans));
				
				session.removeAttribute("Quiz");
				session.removeAttribute("Answer");
				return "redirect:/main";
			}
			
			model.addAttribute("questionAns", ans.get(i-1));
			return "redirect:/quiz/" + i;
		}
		return "error";

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
	
	
	public void setSessionManagerAns() {
		// HttpSession session = request.getSession();
		ObjectMapper objectMapper = new ObjectMapper();
		String quizJson = "";
		if (session.getAttribute("Answer") == null) {
			try {
				quizJson = objectMapper.writeValueAsString(new String[5]);
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
	

}
