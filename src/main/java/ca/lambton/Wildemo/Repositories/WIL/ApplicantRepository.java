package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Applicant;

public interface ApplicantRepository extends CrudRepository<Applicant, Integer>{

	public List<Applicant> findAll();
	
	public Applicant findByEmail(String email);
}
