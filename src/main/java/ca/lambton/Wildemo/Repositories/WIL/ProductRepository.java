package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Applicant;
import ca.lambton.Wildemo.Models.WIL.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{

	public List<Product> findAll();
	
	//public Applicant findByEmail(String email);
}
