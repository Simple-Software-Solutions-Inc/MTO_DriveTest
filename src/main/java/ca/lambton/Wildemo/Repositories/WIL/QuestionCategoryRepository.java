package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Question_Category;

public interface QuestionCategoryRepository extends CrudRepository<Question_Category, Integer>{

	public List<Question_Category> findAll();
	
	
}
