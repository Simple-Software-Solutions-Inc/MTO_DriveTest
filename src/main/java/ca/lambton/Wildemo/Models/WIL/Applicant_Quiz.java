package ca.lambton.Wildemo.Models.WIL;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "applicant_quizzes")
public class Applicant_Quiz {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="quiz_id")
	private Integer quizId;
	
	@NonNull
	@Column(name="quiz_questions")
	private String quizQuestionList;
	
	@NonNull
	@Column(name="quiz_answers")
	private String answersList;
	
	@NonNull
	@Column(name="quiz_result")
	private Double result;
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name = "applicant_id", referencedColumnName = "applicant_id")
	private Applicant applicantId;
	
	
	@Column(name = "quiz_date", insertable = false, updatable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date quizDate;
	
	
	@NonNull
	@ManyToOne 
	@JoinColumn(name = "center_id", referencedColumnName = "center_id")
	private DriveCenter centerId;

}
