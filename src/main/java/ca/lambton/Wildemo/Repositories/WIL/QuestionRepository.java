package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;


import ca.lambton.Wildemo.Models.WIL.Question;

public interface QuestionRepository extends CrudRepository<Question, Integer>{

	public List<Question> findAll();
	
}
