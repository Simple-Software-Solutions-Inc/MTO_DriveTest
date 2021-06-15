package ca.lambton.Wildemo.Models.WIL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Prospect {
	
	@NonNull
	private String password;
	
	@NonNull
	private Applicant applicant;
	
	@NonNull
	private ProofIdentity proof;
	
	
}
