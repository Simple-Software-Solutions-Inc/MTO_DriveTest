package ca.lambton.Wildemo.Models.WIL;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProspectLogin {
	
	@NonNull
	private String email;
	
	@NonNull
	private String password;

}
