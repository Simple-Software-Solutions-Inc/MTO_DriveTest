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
@Table(name = "transactions")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer transaction_id ; 
	
	
	@NonNull
	private String payment_method;
	
	@NonNull
	private String transaction_date;
	
	@NonNull
	private Double amount;
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name = "applicant_id", referencedColumnName = "applicant_id")
	private Applicant applicant_id;
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name = "trans_cat_id", referencedColumnName = "trans_cat_id")
	private TransactionCategory trans_cat_id;
	
	

	@Transient
	public String toString() {
		return this.trans_cat_id + " " + this.applicant_id.toString();
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "Payment Method",  "Transaction Date", "Amount",  "Applicant", "Transaction Category" };
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.transaction_id.toString(), this.payment_method, this.transaction_date, this.amount.toString(), this.applicant_id.toString(), this.trans_cat_id.toString()};
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"transaction_id", "payment_method", "transaction_date", "amount",  "applicant_id", "trans_cat_id"};
		String[] fieldLabel =  { "ID", "Payment Method", "Date", "Amount", "Applicant", "Category"};
		String[] fieldTag = {"number", "text", "date", "number", "select", "select"};
		String[][] fData = new String[6][3];
		for(int i=0; i<fData.length; i++) {
			fData[i][0] = fieldName[i];
			fData[i][1] = fieldLabel[i];
			fData[i][2] = fieldTag[i];
		}

		return fData;
	}

}
