package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.TransactionCategory;

public interface TransactionCategoryRepository extends CrudRepository<TransactionCategory, Integer>{

	public List<TransactionCategory> findAll();
}
