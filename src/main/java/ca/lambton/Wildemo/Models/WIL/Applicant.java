package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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
//	@Size(min = 2, max = 30, message = "Student first name must be 2 - 30 characters")
//	@NotNull(message = "Student must have a first name")
//	@NotBlank(message = "First name can't be blank")
	private String first_name;

	@NonNull
	private String last_name;

	@NonNull
	private String dob;

	@NonNull
	private String nationality;
	
	@NonNull
	private String address;

	@NonNull
	private String email;

	@NonNull
	private String phone_no;

	@NonNull
	private String gender;
	
	@NonNull
	private String height;

	@NonNull
	@ManyToOne // (cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id", referencedColumnName = "location_id")
	private Location location_id;
	
	@NonNull
	@ManyToOne // (cascade = CascadeType.ALL)
	@JoinColumn(name = "proof_id", referencedColumnName = "proof_id")
	private ProofIdentity proof_id;

	@Transient
	public String toString() {
		return this.first_name + " " + this.last_name + " " + this.location_id.getZip_code();
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
