package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Admin;
import ca.lambton.Wildemo.Models.WIL.Applicant;

public interface AdminRepository extends CrudRepository<Admin, Integer>{

	public List<Admin> findAll();
	
	public Admin findByAdminEmail(String email);
}
