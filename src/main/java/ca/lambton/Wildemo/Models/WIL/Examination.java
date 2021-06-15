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
@Table(name = "examinations")
public class Examination {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer test_id; 
	
	
	@NonNull
	private Double test_score;
	
	@NonNull
	private String test_date;
	
	@NonNull
	private String test_result;
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name = "transaction_id", referencedColumnName = "transaction_id")
	private Transaction transaction_id;
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name = "center_id", referencedColumnName = "center_id")
	private DriveCenter center_id;

	@Transient
	public String toString() {
		return this.test_id + " " + this.test_result;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "Score",  "Test Date", "Result", "Applicant", "Center" };
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.test_id.toString(), this.test_score.toString(), this.test_date, this.test_result, this.transaction_id.toString(), this.center_id.toString()};
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"test_id", "test_score", "test_date", "test_result",  "transaction_id", "center_id"};
		String[] fieldLabel =  { "ID", "Test Score", "Test Date", "Result", "TransactionRef", "CenterRef"};
		String[] fieldTag = {"number", "text", "date", "select", "select",  "select"};
		String[][] fData = new String[6][3];
		for(int i=0; i<fData.length; i++) {
			fData[i][0] = fieldName[i];
			fData[i][1] = fieldLabel[i];
			fData[i][2] = fieldTag[i];
		}

		return fData;
	}
	
}
