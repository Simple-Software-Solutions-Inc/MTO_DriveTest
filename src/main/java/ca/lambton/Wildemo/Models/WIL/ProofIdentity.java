package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "proof_identities")
public class ProofIdentity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer proof_id ;
	
	
	@NonNull
	private String serial_no;
	
	
	@NonNull
	private String issue_date;
	
	
	@NonNull
	private String expiry_date;
	
	
	@NonNull
	private String identification_type;
	
	
	@NonNull
	private String issued_location;
	
	
	private String image_file;
	
	
	@Transient
	public String toString() {
		return this.serial_no ;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "Serial No",  "Issue Date", "Expiry Date", "ID Type", "Issued Location"};
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.proof_id.toString(), this.serial_no , this.issue_date, this.expiry_date, this.identification_type, this.issued_location};
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"proof_id", "serial_no", "issue_date", "expiry_date", "identification_type", "issued_location", "image_file"};
		String[] fieldLabel =  { "ID", "Serial No",  "Issue Date", "Expiry Date", "ID Type", "Issued Location", "Upload File"};
		String[] fieldTag = {"number", "text", "date", "date", "text", "text", "file"};
		String[][] fData = new String[7][3];
		for(int i=0; i<fData.length; i++) {
			fData[i][0] = fieldName[i];
			fData[i][1] = fieldLabel[i];
			fData[i][2] = fieldTag[i];
		}

		return fData;
	}
}
