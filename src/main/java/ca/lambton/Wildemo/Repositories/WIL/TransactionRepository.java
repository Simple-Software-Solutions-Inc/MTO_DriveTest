package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Integer>{

	public List<Transaction> findAll();
}
