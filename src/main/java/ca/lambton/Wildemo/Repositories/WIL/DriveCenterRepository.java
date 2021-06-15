package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.DriveCenter;

public interface DriveCenterRepository extends CrudRepository<DriveCenter, Integer>{

	public List<DriveCenter> findAll();
}
