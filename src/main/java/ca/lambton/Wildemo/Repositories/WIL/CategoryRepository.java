package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer>{

	public List<Category> findAll();
}
