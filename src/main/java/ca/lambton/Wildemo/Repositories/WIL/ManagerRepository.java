package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.Manager;

public interface ManagerRepository extends CrudRepository<Manager, Integer>{

	public List<Manager> findAll();
}
