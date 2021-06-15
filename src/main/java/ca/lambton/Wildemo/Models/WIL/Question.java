package ca.lambton.Wildemo.Models.WIL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

}

