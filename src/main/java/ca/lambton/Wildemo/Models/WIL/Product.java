package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Column;
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
@Table(name = "products")
public class Product {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="product_id")
	private Integer productId;
	
	@NonNull
	@Size(max = 20, message = "Name must not greater than 20 characters")
	@NotNull(message = "Name must have a entered")
	@NotBlank(message = "Name can't be blank")
	@Column(name="name")
	private String name;
	
	@NonNull
	@Size(max = 40, message = "Description must not greater than 40 characters")
	@NotNull(message = "Description must have a name")
	@NotBlank(message = "Description name can't be blank")
	@Column(name="description")
	private String description;
	
	@NonNull
	@Column(name="price")
	private Double price;
	
	@NonNull
	@Column(name="image_path")
	private String image_file;
	
	
	@Transient
	public String toString() {
		return this.name + " " + this.description + " " + this.price;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "Name",  "Description",  "Price"};
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.productId.toString(), this.name , this.description, this.price.toString() };
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"productId", "name", "description", "price", "image_file"};
		String[] fieldLabel =  { "ID", "Name", "Description", "Price", "Image"};
		String[] fieldTag = {"number", "text", "text", "number", "file"};
		String[][] fData = new String[5][3];
		for(int i=0; i<fData.length; i++) {
			fData[i][0] = fieldName[i];
			fData[i][1] = fieldLabel[i];
			fData[i][2] = fieldTag[i];
		}

		return fData;
	}
}
