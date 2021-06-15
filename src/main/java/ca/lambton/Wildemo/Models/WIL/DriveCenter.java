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
@Table(name = "drive_centers")
public class DriveCenter {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer center_id ; 
	
	
	@NonNull
	private String contact_no;
	
	@NonNull
	private String address;
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name = "manager_id", referencedColumnName = "manager_id")
	private Manager manager_id;
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name = "location_id", referencedColumnName = "location_id")
	private Location location_id;
	
	

	@Transient
	public String toString() {
		return this.center_id + " " + this.address;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "Contact",  "Manager", "Address",  "Zip" };
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.center_id.toString(), this.contact_no, this.manager_id.toString(), this.address, this.location_id.toString()};
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"center_id", "contact_no", "manager_id", "address",  "location_id"};
		String[] fieldLabel =  { "ID", "Contact No", "Manager", "Address", "Zip"};
		String[] fieldTag = {"number", "text", "select", "text", "select"};
		String[][] fData = new String[5][3];
		for(int i=0; i<fData.length; i++) {
			fData[i][0] = fieldName[i];
			fData[i][1] = fieldLabel[i];
			fData[i][2] = fieldTag[i];
		}

		return fData;
	}
}
