package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "managers")
public class Manager {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer manager_id;
	
	
	@NonNull
//	@Size(min = 2, max = 30, message = "Student last name must be 2 - 30 characters")
//	@NotNull(message = "Student must have a last name")
//	@NotBlank(message = "Last name can't be blank")
	private String first_name;
	
	
	@NonNull
//	@Size(min = 2, max = 30, message = "Student last name must be 2 - 30 characters")
//	@NotNull(message = "Student must have a last name")
//	@NotBlank(message = "Last name can't be blank")
	private String last_name;
	
	
	@Transient
	public String toString() {
		return this.first_name + " " + this.last_name ;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "First Name",  "Last Name"};
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.manager_id.toString(), this.first_name , this.last_name };
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"manager_id", "first_name", "last_name"};
		String[] fieldLabel =  { "ID", "First Name", "Last Name"};
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
