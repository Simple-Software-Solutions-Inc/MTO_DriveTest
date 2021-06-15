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
@Table(name = "locations")
public class Location {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer location_id;
	
	@NonNull
	@Size(max = 8, message = "Zip code must not greater than 8 characters")
	@NotNull(message = "Zip code must have a entered")
	@NotBlank(message = "Zip code can't be blank")
	private String zip_code;
	
	@NonNull
	@Size(max = 40, message = "City name must not greater than 40 characters")
	@NotNull(message = "City must have a name")
	@NotBlank(message = "City name can't be blank")
	private String city;
	
	@NonNull
	@Size(max = 40, message = "Province name can not be greater than 30 characters")
	@NotNull(message = "Province must have a last name")
	@NotBlank(message = "Province name can't be blank")
	private String province;
	
	
	@Transient
	public String toString() {
		return this.zip_code + " " + this.city + " " + this.province;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "Zip",  "City",  "Province"};
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.location_id.toString(), this.zip_code , this.city, this.province };
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"location_id", "zip_code", "city", "province"};
		String[] fieldLabel =  { "ID", "Zip Code", "City", "Province"};
		String[] fieldTag = {"number", "text", "text", "text"};
		String[][] fData = new String[4][3];
		for(int i=0; i<fData.length; i++) {
			fData[i][0] = fieldName[i];
			fData[i][1] = fieldLabel[i];
			fData[i][2] = fieldTag[i];
		}

		return fData;
	}
}
