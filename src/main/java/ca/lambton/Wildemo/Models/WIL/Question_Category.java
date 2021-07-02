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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "question_categories")
public class Question_Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="ques_cat_id")
	private Integer quesCatId;
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name="question_id", referencedColumnName ="ques_id")
	private Question questionId;
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name="category_id", referencedColumnName ="cat_id")
	private Category categoryId;

	
	@Transient
	public String toString() {
		return this.questionId.getQues_id() + " " + this.categoryId.getName() ;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "Name",  "Description"};
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.quesCatId.toString(), this.questionId.getQuestion() , this.categoryId.getName() };
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"quesCatId", "questionId", "categoryId"};
		String[] fieldLabel =  { "ID", "Name", "Description"};
		String[] fieldTag = {"number", "select", "select"};
		String[][] fData = new String[3][3];
		for(int i=0; i<fData.length; i++) {
			fData[i][0] = fieldName[i];
			fData[i][1] = fieldLabel[i];
			fData[i][2] = fieldTag[i];
		}

		return fData;
	}
}
