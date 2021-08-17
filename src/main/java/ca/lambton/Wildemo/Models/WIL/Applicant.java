package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "applicants")
public class Applicant {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer applicant_id;

	@NonNull
	@Size(min = 2, max = 30, message = "First name must be 2 - 30 characters")
	@NotNull(message = "Applicant must have a first name")
	@NotBlank(message = "First name can't be blank")
	private String first_name;

	@NonNull
	@Size(min = 2, max = 30, message = "Last name must be 2 - 30 characters")
	@NotNull(message = "Applicant must have a Last name")
	@NotBlank(message = "Last name can't be blank")
	private String last_name;

	@NonNull
	@NotNull(message = "Applicant must have a Date of Birth")
	@NotBlank(message = "Date of Birth can't be blank")
	private String dob;

	@NonNull
	@Size(min = 2, max = 30, message = "Nationality must be 2 - 30 characters")
	@NotNull(message = "Applicant must have a nationality")
	@NotBlank(message = "Nationality name can't be blank")
	private String nationality;
	
	@NonNull
	@Size(min = 2, max = 30, message = "Address must be 2 - 30 characters")
	@NotNull(message = "Applicant must have a address")
	@NotBlank(message = "Address name can't be blank")
	private String address;

	@NonNull
	@Email()
	@NotNull(message = "Applicant must have a email")
	@NotBlank(message = "Email can't be blank")
	private String email;

	@NonNull
	@Size(min = 7, max = 10, message = "Contact number must be 7 - 10 characters")
	@NotNull(message = "Applicant must have a contact number")
	@NotBlank(message = "Contact number can't be blank")
	private String phone_no;

	@NonNull
	@NotNull(message = "Applicant must have a gender")
	@NotBlank(message = "Gender can't be blank")
	private String gender;
	
	@NonNull
	@NotNull(message = "Applicant must enter a height")
	@NotBlank(message = "Height can't be blank")
	private String height;

	@NonNull
	@ManyToOne // (cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id", referencedColumnName = "location_id")
//	@NotNull(message = "Applicant must have a zip code")
//	@NotBlank(message = "Zip Code can't be blank")
	@Valid
	private Location location_id;
	
	@NonNull
	@ManyToOne // (cascade = CascadeType.ALL)
	@JoinColumn(name = "proof_id", referencedColumnName = "proof_id")
	private ProofIdentity proof_id;

	@Transient
	public String toString() {
		return this.first_name + " " + this.last_name + " " + this.location_id;
	}

	@Transient
	public static String[] fieldNames() {

		String[] fnames = { "ID", "Name", "Address" };
		return fnames;
	}

	@Transient
	public String[] fieldData() {
		String[] fData = {
				this.applicant_id.toString(), this.first_name + " " + this.last_name,
				this.dob, this.nationality, this.email, this.phone_no, this.gender, this.address, this.location_id.toString() };
		return fData;
	}

	// modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = { "applicant_id", "first_name", "last_name", "dob", "nationality", "email", "phone_no", "gender", "height", "address", "location_id", "proof_id"};
		String[] fieldLabel = { "ID", "First Name", "Last Name", "Date of Birth", "Nationality", "Email", "Phone Number", "Gender", "Height", "Address", "Zip", "Proof"};
		String[] fieldTag = { "number", "text", "text",  "date", "text", "text", "text", "select","text", "text", "select", "select"};
		
		String[][] fData = new String[12][];
		for (int i = 0; i < fData.length; i++) {

			fData[i] = new String[] {fieldName[i], fieldLabel[i], fieldTag[i]};
		}
		return fData;
	}
}
