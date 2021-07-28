package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Admin;
import ca.lambton.Wildemo.Models.WIL.AdminPassword;
import ca.lambton.Wildemo.Models.WIL.Applicant;
import ca.lambton.Wildemo.Models.WIL.Password;

public interface AdminPasswordRepository extends CrudRepository<AdminPassword, Integer>{

	public List<AdminPassword> findAll();
	
	public List<AdminPassword> findByAdminId(Admin adminId);
}
