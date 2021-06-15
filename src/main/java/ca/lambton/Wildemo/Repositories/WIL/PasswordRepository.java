package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Applicant;
import ca.lambton.Wildemo.Models.WIL.Password;

public interface PasswordRepository extends CrudRepository<Password, Integer>{

	public List<Password> findAll();
	
	public List<Password> findByApplicantId(Applicant applicantId);
}
