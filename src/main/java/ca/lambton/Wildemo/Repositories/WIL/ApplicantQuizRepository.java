package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;


import ca.lambton.Wildemo.Models.WIL.Applicant_Quiz;

public interface ApplicantQuizRepository extends CrudRepository<Applicant_Quiz, Integer>{

	public List<Applicant_Quiz> findAll();
	
	
}
