package ca.lambton.Wildemo.Models.WIL;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProspectLogin {
	
	@NonNull
	@NotBlank(message= "Username or email can not be empty")
	private String email;
	
	@NonNull
	@NotBlank(message= "Password can not be empty")
	private String password;

}
