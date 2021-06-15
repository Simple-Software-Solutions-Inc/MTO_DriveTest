package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;


import ca.lambton.Wildemo.Models.WIL.Location;

public interface LocationRepository extends CrudRepository<Location, Integer>{

	public List<Location> findAll();
}
