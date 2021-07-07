package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Column;
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
@Table(name = "questions")
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer ques_id;
	
	@NonNull
	@Column(name="question")
	private String question;
	
	@NonNull
	private String answer;
	
	@NonNull
	@Column(name="question_type")
	private String questionType;
	
	@NonNull
	@Column(name="media_type")
	private String mediaType;
	
	@NonNull
	@Column(name="media_path")
	private String mediaPath;

	
	@Transient
	public String toString() {
		return this.ques_id + " " + this.question;
	}
	
	@Transient
	public static String[] fieldNames() {
		
		String[] fnames = { "ID", "Quesstion",  "Answer" };
		return fnames;
	}
	
	@Transient
	public String[] fieldData() {
		String[] fData =  { this.ques_id.toString(), this.question, this.answer};
		return fData;
	}
	
	//modify arrays to customize the fields shown on form
	@Transient
	public static String[][] formBuilder() {
		String[] fieldName = {"ques_id", "question", "answer"};
		String[] fieldLabel =  { "ID", "Question", "Answer"};
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

