package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "admin_passwords")
public class AdminPassword {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="admin_pass_id")
	private Integer password_id;
	
	@NonNull
	@Column(name="password")
	private String password;
	
	@NonNull
	@Column(name="expiry_date")
	private String expiryDate;
	
	@ManyToOne // (cascade = CascadeType.ALL)
	@JoinColumn(name = "admin_id", referencedColumnName = "admin_id")
	private Admin adminId;

}
