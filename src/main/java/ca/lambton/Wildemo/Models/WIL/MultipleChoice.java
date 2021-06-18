package ca.lambton.Wildemo.Models.WIL;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.*;
@Getter
@ToString
@EqualsAndHashCode
@Data
@NoArgsConstructor
//private all-argument constructor and builder method
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MultipleChoice {
	
	
		    private String id;
		   
		    private String question;
		 
		    private String option_A;
		  
		    private String option_B;
		   
		    private String option_C;
		  
		    private String option_D;
		   
		    private String answer;
		
		    private String mediaType;
		    
		    private String mediaPath;
		
		
		/**
		 * private method that creates an BlogPosts instance from the json object properties
		 * @param id
		 * @param authorId
		 * @param postContent
		 * @return
		 */
		@Builder
		@JsonCreator
		private static MultipleChoice of(
				@JsonProperty("Number")  String id,
				@JsonProperty("Question") String question,
				@JsonProperty("Option_A") String option_A,
				@JsonProperty("Option_B") String option_B,
				@JsonProperty("Option_C") String option_C,
				@JsonProperty("Option_D") String option_D,
				@JsonProperty("Answer") String answer,
				@JsonProperty("Media_Type") String mediaType,
				@JsonProperty("Media_Path") String mediaPath
				)
		{
			//call the private constructor
			return new MultipleChoice(id, question, option_A, option_B, option_C, option_D, answer, mediaType, mediaPath);
		}

}
