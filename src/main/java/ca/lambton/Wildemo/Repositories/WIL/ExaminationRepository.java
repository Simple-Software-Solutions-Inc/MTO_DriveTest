package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Examination;

public interface ExaminationRepository extends CrudRepository<Examination, Integer>{

	public List<Examination> findAll();
}
