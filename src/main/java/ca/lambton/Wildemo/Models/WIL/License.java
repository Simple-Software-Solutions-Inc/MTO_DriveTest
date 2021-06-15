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
@Table(name = "licenses")
public class License {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer license_id; 
	
	@NonNull
	private String license_no;
	
	@NonNull
	private String issue_date;
	
	@NonNull
	private String expiry_date;
	
	@NonNull
	private String license_class;
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name = "applicant_id", referencedColumnName = "applicant_id")
	private Applicant applicant_id;

	@Transient
	public String toString() {
		return this.license_class + " " + this.license_no;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "License No",  "Issue Date", "Expiry Date", "License Class", "Applicant" };
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.license_id.toString(), this.license_no, this.issue_date, this.expiry_date, this.license_class, this.applicant_id.getFirst_name()};
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"license_id", "license_no", "issue_date", "expiry_date",  "license_class", "applicant_id"};
		String[] fieldLabel =  { "ID", "License no", "Issue Date", "Expiry Date", "License Class", "Applicant" };
		String[] fieldTag = {"number", "text",  "date", "date", "text", "select"};
		String[][] fData = new String[6][3];
		for(int i=0; i<fData.length; i++) {
			fData[i][0] = fieldName[i];
			fData[i][1] = fieldLabel[i];
			fData[i][2] = fieldTag[i];
		}

		return fData;
	}
	

}
