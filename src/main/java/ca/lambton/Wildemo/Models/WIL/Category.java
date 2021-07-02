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
@Table(name = "categories")
public class Category{

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer cat_id;
	
	@NonNull
	private String name ;
	
	@NonNull
	private String description;
	
	@Transient
	public String toString() {
		return this.name + " " + this.description ;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "Name",  "Description"};
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.cat_id.toString(), this.name , this.description };
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"cat_id", "name", "description"};
		String[] fieldLabel =  { "ID", "Name", "Description"};
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
