package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "passwords")
public class Password {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer password_id;
	
	@NonNull
	private String password;
	
	@NonNull
	private String expiryDate;
	
	@ManyToOne // (cascade = CascadeType.ALL)
	@JoinColumn(name = "applicant_id", referencedColumnName = "applicant_id")
	private Applicant applicantId;

}
