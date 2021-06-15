package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.License;

public interface LicenseRepository extends CrudRepository<License, Integer>{

	public List<License> findAll();
}
