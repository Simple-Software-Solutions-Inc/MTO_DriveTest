package ca.lambton.Wildemo.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.lambton.Wildemo.Models.WIL.Applicant;
import ca.lambton.Wildemo.Models.WIL.Applicant_Quiz;
import ca.lambton.Wildemo.Models.WIL.Question;
import ca.lambton.Wildemo.Repositories.WIL.ApplicantQuizRepository;
import ca.lambton.Wildemo.Repositories.WIL.ApplicantRepository;
import ca.lambton.Wildemo.Repositories.WIL.QuestionRepository;

@RestController
public class DriveTestRestController {

	@Autowired
	private QuestionRepository questionDb;
	
	@Autowired
	private ApplicantRepository applicantDb;

	@Autowired
	private ApplicantQuizRepository applicantQuizDb;
	
	@Scheduled(fixedRate = 3000) 
	public void myScheduledMethod() { 
		
	}
	
	@GetMapping("/results/graduates")
	public List<Applicant> driveTestGraduate() {
		
		List<Applicant> applicant = applicantQuizDb.findAll().stream().filter(x-> x.getResult()>=80).map(x-> x.getApplicantId()).distinct().collect(Collectors.toList());
		
		return applicant;
	}
	
	@GetMapping("/results/graduates/passrate")
	public int[] driveTestPassRate() {
		int numAttempts =  applicantQuizDb.findAll().size();
		int numFailAttempts = applicantQuizDb.findAll().stream().filter(x-> x.getResult()<80).collect(Collectors.toList()).size();
		int numPassAttempts = applicantQuizDb.findAll().stream().filter(x-> x.getResult()>=80).collect(Collectors.toList()).size();
		//List<Applicant> applicant = applicantQuizDb.findAll().stream().filter(x-> x.getResult()>=80).map(x-> x.getApplicantId()).distinct().collect(Collectors.toList());
		int[] passrate ={numFailAttempts, numPassAttempts };
		return passrate;
	}
}
