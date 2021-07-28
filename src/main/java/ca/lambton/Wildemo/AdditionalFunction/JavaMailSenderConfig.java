package ca.lambton.Wildemo.AdditionalFunction;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;



public class JavaMailSenderConfig {

	@Value("${spring.mail.host}")
	private String mailHost;
	
	@Value("${spring.mail.username}")
	private String mailUsername;
	
	@Value("${spring.mail.password}")
	private String mailPassword;
	
	
	@Value("${spring.mail.properties.mail.smtp.socketFactory.port}")
	private Integer mailPort;
	
	//Method creates a page counter bean dependency for the project
		@Bean
		public JavaMailSender getJavaMailSender() {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			
			mailSender.setHost(mailHost);
		    mailSender.setPort(mailPort);
		    
		    mailSender.setUsername(mailUsername);
		    mailSender.setPassword(mailPassword);
		    
		    Properties props = mailSender.getJavaMailProperties();
		    props.put("mail.transport.protocol", "smtp");
		    props.put("mail.smtp.auth", "true");
		    props.put("mail.smtp.starttls.enable", "true");
		    props.put("mail.debug", "true");
		    
		    
			return mailSender;
		}
}
