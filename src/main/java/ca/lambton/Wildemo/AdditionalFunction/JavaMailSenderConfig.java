package ca.lambton.Wildemo.AdditionalFunction;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;



public class JavaMailSenderConfig {

	
	//Method creates a page counter bean dependency for the project
		@Bean
		public JavaMailSender getJavaMailSender() {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			
			mailSender.setHost("smtp.gmail.com");
		    mailSender.setPort(465);
		    
		    mailSender.setUsername("");
		    mailSender.setPassword("");
		    
		    Properties props = mailSender.getJavaMailProperties();
		    props.put("mail.transport.protocol", "smtp");
		    props.put("mail.smtp.auth", "true");
		    props.put("mail.smtp.starttls.enable", "true");
		    props.put("mail.debug", "true");
		    
		    
			return mailSender;
		}
}
