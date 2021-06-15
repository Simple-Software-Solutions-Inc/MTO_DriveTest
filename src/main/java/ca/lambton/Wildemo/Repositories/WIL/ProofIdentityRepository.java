package ca.lambton.Wildemo.Repositories.WIL;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.lambton.Wildemo.Models.WIL.ProofIdentity;

public interface ProofIdentityRepository extends CrudRepository<ProofIdentity, Integer>{

	public List<ProofIdentity> findAll();
}
