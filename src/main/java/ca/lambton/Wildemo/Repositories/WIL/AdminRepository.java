package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import javax.annotation.PreDestroy;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Admin;
import ca.lambton.Wildemo.Models.WIL.Applicant;

public interface AdminRepository extends CrudRepository<Admin, Integer>{

	public List<Admin> findAll();
	
	public Admin findByAdminEmail(String email);
	
//	public Admin findByAdminNameAdminEmail(String name, String email);
	
	
//	private DbConnection dbConnection;
//    @PreDestroy
//    public void preDestroy() {
//        dbConnection.close();
//    }
}
