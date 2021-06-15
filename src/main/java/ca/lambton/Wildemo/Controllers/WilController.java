package ca.lambton.Wildemo.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import ca.lambton.Wildemo.Models.WIL.Applicant;
import ca.lambton.Wildemo.Models.WIL.DriveCenter;
import ca.lambton.Wildemo.Models.WIL.Examination;
import ca.lambton.Wildemo.Models.WIL.License;
import ca.lambton.Wildemo.Models.WIL.Location;
import ca.lambton.Wildemo.Models.WIL.Manager;
import ca.lambton.Wildemo.Models.WIL.ProofIdentity;
import ca.lambton.Wildemo.Models.WIL.Transaction;
import ca.lambton.Wildemo.Models.WIL.TransactionCategory;
import ca.lambton.Wildemo.Repositories.WIL.ApplicantRepository;
import ca.lambton.Wildemo.Repositories.WIL.DriveCenterRepository;
import ca.lambton.Wildemo.Repositories.WIL.ExaminationRepository;
import ca.lambton.Wildemo.Repositories.WIL.LicenseRepository;
import ca.lambton.Wildemo.Repositories.WIL.LocationRepository;
import ca.lambton.Wildemo.Repositories.WIL.ManagerRepository;
import ca.lambton.Wildemo.Repositories.WIL.ProofIdentityRepository;
import ca.lambton.Wildemo.Repositories.WIL.TransactionCategoryRepository;
import ca.lambton.Wildemo.Repositories.WIL.TransactionRepository;

@Controller
public class WilController {

	@Autowired
	private ApplicantRepository applicantDb;
	
	@Autowired
	private DriveCenterRepository driveCenterDb;

	@Autowired
	private ExaminationRepository examinationDb;

	@Autowired
	private LicenseRepository licenseDb;

	@Autowired
	private LocationRepository locationDb;

	@Autowired
	private ProofIdentityRepository proofIdentityDb;

	@Autowired
	private TransactionRepository transactionDb;

	@Autowired
	private TransactionCategoryRepository transactionCategoryDb;

	@Autowired
	private ManagerRepository managerDb;
	
	@GetMapping("/app/managers/add-new")
	public String newManager(Model model) {
		model.addAttribute("modelClass", new Manager());	
		model.addAttribute("destination", "/app/managers/add-new/");
		
		return "layouts/form_components/main_form";
	}
	
	
	@PostMapping("/app/managers/add-new")
	public String newManager(@Valid @ModelAttribute("modelClass") Manager manager, BindingResult bindingResult, ModelMap model) {

		if (bindingResult.hasErrors()) {
			return "layouts/form_components/main_form";
		}

		
		managerDb.save(manager);
		
		return "redirect:/app/managers";
	}
	
	@GetMapping("/app/locations/add-new")
	public String newLocation(Model model) {
		model.addAttribute("modelClass", new Location());	
		model.addAttribute("destination", "/app/locations/add-new/");
		
		return "layouts/form_components/main_form";
	}
	
	
	@PostMapping("/app/locations/add-new")
	public String newLocation(@Valid @ModelAttribute("modelClass") Location location, BindingResult bindingResult, ModelMap model) {

		if (bindingResult.hasErrors()) {
			return "layouts/form_components/main_form";
		}

		
		locationDb.save(location);
		
		return "redirect:/app/locations";
	}
	
	
	@GetMapping("/app/applicants/add-new")
	public String newApplicant(Model model) {
		model.addAttribute("modelClass", new Applicant());	
		model.addAttribute("destination", "/app/applicants/add-new/");
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
		
		List<ModelMap> lstProof = proofIdentityDb.findAll().stream().map(x -> {
			ModelMap std = new ModelMap();
			std.addAttribute("Name", x.toString());
			std.addAttribute("Num", x.getProof_id());
			return std;
		}).collect(Collectors.toList());
		
		
		lstLocation.add(0, emptyMap);
		lstProof.add(0, emptyMap);
		String[] gender = {"F", "M"};
		ModelMap maleMap = new ModelMap();
		maleMap.addAttribute("Name", "Male");
		maleMap.addAttribute("Num", "M");
		ModelMap femaleMap = new ModelMap();
		femaleMap.addAttribute("Name", "Female");
		femaleMap.addAttribute("Num", "F");
		List<ModelMap> lstGender = new ArrayList<ModelMap>();
		lstGender.add(femaleMap);
		lstGender.add(maleMap);
		lstGender.add(0, emptyMap);
		ModelMap foreignModel = new ModelMap();
		foreignModel.addAttribute("Zip", lstLocation);
		foreignModel.addAttribute("Gender", lstGender);
		foreignModel.addAttribute("Proof", lstProof);
		model.addAttribute("foreignModel", foreignModel);
		return "layouts/form_components/main_form";
	}
	
	
	@PostMapping("/app/applicants/add-new")
	public String newApplicant(@Valid @ModelAttribute("modelClass") Applicant applicant, 
			BindingResult bindingResult, ModelMap model) {

		if (bindingResult.hasErrors()) {
			return "layouts/form_components/main_form";
		}
		
		 applicantDb.save(applicant);
		
		return "redirect:/app/applicants";
	}
	
	
	
	
	@GetMapping("/app/licenses/add-new")
	public String newLicense(Model model) {
		model.addAttribute("modelClass", new License());	
		model.addAttribute("destination", "/app/licenses/add-new/");
		model.addAttribute("foreign", "foreign");
		ModelMap emptyMap = new ModelMap();
		emptyMap.addAttribute("Name", "---------");
		emptyMap.addAttribute("Num", "");
		
		
		List<ModelMap> lstApplicant = applicantDb.findAll().stream().map(x -> {
			ModelMap std = new ModelMap();
			std.addAttribute("Name", x.toString());
			std.addAttribute("Num", x.getApplicant_id());
			return std;
		}).collect(Collectors.toList());
		
		
		lstApplicant.add(0, emptyMap);
	
		
		ModelMap foreignModel = new ModelMap();
		foreignModel.addAttribute("Applicant", lstApplicant);
		model.addAttribute("foreignModel", foreignModel);
		return "layouts/form_components/main_form";
	}
	
	
	@PostMapping("/app/licenses/add-new")
	public String newLicense(@Valid @ModelAttribute("modelClass") License license, 
			BindingResult bindingResult, ModelMap model) {

		if (bindingResult.hasErrors()) {
			return "layouts/form_components/main_form";
		}
		
		 licenseDb.save(license);
		
		return "redirect:/app/licenses";
	}
	
	
	@GetMapping("/app/proof-identities/add-new")
	public String newProof(Model model) {
		model.addAttribute("modelClass", new ProofIdentity());	
		model.addAttribute("destination", "/app/proof-identities/add-new/");

		return "layouts/form_components/main_form";
	}
	
	
	@PostMapping("/app/proof-identities/add-new")
	public String  newProof(@Valid @ModelAttribute("modelClass") ProofIdentity proofIdentity, 
			BindingResult bindingResult, ModelMap model) {

		if (bindingResult.hasErrors()) {
			return "layouts/form_components/main_form";
		}
		
		 proofIdentityDb.save(proofIdentity);
		
		return "redirect:/app/proof-identities";
	}
	
	@GetMapping("/app/transaction-categories/add-new")
	public String newTransactionCategory(Model model) {
		model.addAttribute("modelClass", new TransactionCategory());	
		model.addAttribute("destination", "/app/transaction-categories/add-new/");

		return "layouts/form_components/main_form";
	}
	
	
	@PostMapping("/app/transaction-categories/add-new")
	public String  newTransactionCategory(@Valid @ModelAttribute("modelClass") TransactionCategory transactionCategory, 
			BindingResult bindingResult, ModelMap model) {

		if (bindingResult.hasErrors()) {
			return "layouts/form_components/main_form";
		}
		
		transactionCategoryDb.save(transactionCategory);
		
		return "redirect:/app/transaction-categories";
	}
	
	
	@GetMapping("/app/transactions/add-new")
	public String newTransaction(Model model) {
		model.addAttribute("modelClass", new Transaction());	
		model.addAttribute("destination", "/app/transactions/add-new/");
		model.addAttribute("foreign", "foreign");
		ModelMap emptyMap = new ModelMap();
		emptyMap.addAttribute("Name", "---------");
		emptyMap.addAttribute("Num", "");
		
		
		List<ModelMap> lstApplicant = applicantDb.findAll().stream().map(x -> {
			ModelMap std = new ModelMap();
			std.addAttribute("Name", x.toString());
			std.addAttribute("Num", x.getApplicant_id());
			return std;
		}).collect(Collectors.toList());
		
		
		lstApplicant.add(0, emptyMap);
	
		
		List<ModelMap> lstTransCat = transactionCategoryDb.findAll().stream().map(x -> {
			ModelMap std = new ModelMap();
			std.addAttribute("Name", x.toString());
			std.addAttribute("Num", x.getTrans_cat_id());
			return std;
		}).collect(Collectors.toList());
		
		
		lstTransCat.add(0, emptyMap);
		
		ModelMap foreignModel = new ModelMap();
		foreignModel.addAttribute("Applicant", lstApplicant);
		foreignModel.addAttribute("Category", lstTransCat);
		model.addAttribute("foreignModel", foreignModel);
		return "layouts/form_components/main_form";
	}
	
	
	@PostMapping("/app/transactions/add-new")
	public String newTransaction(@Valid @ModelAttribute("modelClass") Transaction transaction, 
			BindingResult bindingResult, ModelMap model) {

		if (bindingResult.hasErrors()) {
			return "layouts/form_components/main_form";
		}
		
		transactionDb.save(transaction);
		
		return "redirect:/app/transactions";
	}
	
	
	@GetMapping("/app/Examination/add-new")
	public String newDriveCenter(Model model) {
		model.addAttribute("modelClass", new DriveCenter());	
		model.addAttribute("destination", "/app/drive-centers/add-new/");
		model.addAttribute("foreign", "foreign");
		ModelMap emptyMap = new ModelMap();
		emptyMap.addAttribute("Name", "---------");
		emptyMap.addAttribute("Num", "");
		
		
		List<ModelMap> lstManager = managerDb.findAll().stream().map(x -> {
			ModelMap std = new ModelMap();
			std.addAttribute("Name", x.toString());
			std.addAttribute("Num", x.getManager_id());
			return std;
		}).collect(Collectors.toList());
		
		
		lstManager.add(0, emptyMap);
	
		
		List<ModelMap> lstlocation = locationDb.findAll().stream().map(x -> {
			ModelMap std = new ModelMap();
			std.addAttribute("Name", x.toString());
			std.addAttribute("Num", x.getLocation_id());
			return std;
		}).collect(Collectors.toList());
		
		
		lstlocation.add(0, emptyMap);
		
		ModelMap foreignModel = new ModelMap();
		foreignModel.addAttribute("Manager", lstManager);
		foreignModel.addAttribute("Zip", lstlocation);
		model.addAttribute("foreignModel", foreignModel);
		return "layouts/form_components/main_form";
	}
	
	
	@PostMapping("/app/drive-centers/add-new")
	public String newDriveCenter(@Valid @ModelAttribute("modelClass") DriveCenter driveCenter, 
			BindingResult bindingResult, ModelMap model) {

		if (bindingResult.hasErrors()) {
			return "layouts/form_components/main_form";
		}
		
		driveCenterDb.save(driveCenter);
		
		return "redirect:/app/drive-centers";
	}
	
	
	@GetMapping("/app/examinations/add-new")
	public String newExamination(Model model) {
		model.addAttribute("modelClass", new Examination());	
		model.addAttribute("destination", "/app/examinations/add-new/");
		model.addAttribute("foreign", "foreign");
		ModelMap emptyMap = new ModelMap();
		emptyMap.addAttribute("Name", "---------");
		emptyMap.addAttribute("Num", "");
		
		
		List<ModelMap> lstTransaction = transactionDb.findAll().stream().map(x -> {
			ModelMap std = new ModelMap();
			std.addAttribute("Name", x.toString());
			std.addAttribute("Num", x.getTransaction_id());
			return std;
		}).collect(Collectors.toList());
		
		
		lstTransaction.add(0, emptyMap);
	
		
		List<ModelMap> lstdriveCenter = driveCenterDb.findAll().stream().map(x -> {
			ModelMap std = new ModelMap();
			std.addAttribute("Name", x.toString());
			std.addAttribute("Num", x.getCenter_id());
			return std;
		}).collect(Collectors.toList());
		
		
		lstdriveCenter.add(0, emptyMap);
		
		ModelMap passMap = new ModelMap();
		passMap.addAttribute("Name", "Pass");
		passMap.addAttribute("Num", "Pass");
		ModelMap failMap = new ModelMap();
		failMap.addAttribute("Name", "Fail");
		failMap.addAttribute("Num", "Fail");
		List<ModelMap> lstResult = new ArrayList<ModelMap>();
		lstResult.add(failMap);
		lstResult.add(passMap);
		lstResult.add(0, emptyMap);
		
		
		ModelMap foreignModel = new ModelMap();
		foreignModel.addAttribute("TransactionRef", lstTransaction);
		foreignModel.addAttribute("CenterRef", lstdriveCenter);
		foreignModel.addAttribute("Result", lstResult);
		model.addAttribute("foreignModel", foreignModel);
		return "layouts/form_components/main_form";
	}
	
	
	@PostMapping("/app/examinations/add-new")
	public String newExamination(@Valid @ModelAttribute("modelClass") Examination examination, 
			BindingResult bindingResult, ModelMap model) {

		if (bindingResult.hasErrors()) {
			return "layouts/form_components/main_form";
		}
		
		examinationDb.save(examination);
		
		return "redirect:/app/examinations";
	}
}
