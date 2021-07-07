package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
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
@Table(name = "admins")
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="admin_id")
	private Integer adminId;

	@NonNull
	@Size(min = 2, max = 30, message = "Admin name must be 2 - 30 characters")
	@NotNull(message = "Admin must have a name")
	@NotBlank(message = "Admin name can't be blank")
	@Column(name="admin_name")
	private String admin_name;

	@NonNull
	@Size(min = 2, max = 30, message = "Admin role must be 2 - 30 characters")
	@NotNull(message = "Admin must have a role")
	@NotBlank(message = "Admin role can't be blank")
	@Column(name="admin_role")
	private String admin_role;

	@NonNull
	@Email()
	@NotNull(message = "Admin must have a email")
	@NotBlank(message = "Admin Email can't be blank")
	@Column(name="admin_email")
	private String admin_email;

	@NonNull
	@NotNull(message = "Admin must have a status")
	@NotBlank(message = "Admin status can't be blank")
	@Column(name="admin_status")
	private String admin_status;


	@Transient
	public String toString() {
		return this.admin_name + " " + this.admin_role + " " + this.admin_status;
	}

	@Transient
	public static String[] fieldNames() {

		String[] fnames = { "ID", "Name", "Role", "Status", "Email" };
		return fnames;
	}

	@Transient
	public String[] fieldData() {
		String[] fData = {
				this.adminId.toString(), this.admin_name + " " + this.admin_role,
				this.admin_status, this.admin_email };
		return fData;
	}

	// modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = { "adminId", "admin_name", "admin_role", "admin_status", "admin_email"};
		String[] fieldLabel = { "ID", "Admin name", "Role", "Status", "Email"};
		String[] fieldTag = { "number", "text", "text", "text", "text"};
		
		String[][] fData = new String[5][];
		for (int i = 0; i < fData.length; i++) {

			fData[i] = new String[] {fieldName[i], fieldLabel[i], fieldTag[i]};
		}
		return fData;
	}
}
