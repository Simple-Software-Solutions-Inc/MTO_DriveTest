package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "transaction_categories")
public class TransactionCategory{

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer trans_cat_id;
	
	@NonNull
	private String transaction_type ;
	
	@NonNull
	private String transaction_category;
	
	@Transient
	public String toString() {
		return this.transaction_type + " " + this.transaction_category ;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "Type",  "Category"};
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.trans_cat_id.toString(), this.transaction_type , this.transaction_category };
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"trans_cat_id", "transaction_type", "transaction_category"};
		String[] fieldLabel =  { "ID", "Type", "Category"};
		String[] fieldTag = {"number", "text", "text"};
		String[][] fData = new String[3][3];
		for(int i=0; i<fData.length; i++) {
			fData[i][0] = fieldName[i];
			fData[i][1] = fieldLabel[i];
			fData[i][2] = fieldTag[i];
		}

		return fData;
	}
}
