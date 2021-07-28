package ca.lambton.Wildemo.Models.WIL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Prospect {
	
	@NonNull
	@NotBlank(message = "Enter a password")
	private String password;
	
	@NonNull
	@Valid
	private Applicant applicant;
	
	@NonNull
	@Valid
	private ProofIdentity proof;
}
