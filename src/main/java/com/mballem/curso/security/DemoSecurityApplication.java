package com.mballem.curso.security;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class DemoSecurityApplication {
	
	public static void main(String[] args) {
		/*
		 class used to generate an encrypted password for implementation testing purposes only
		 */
		System.out.println(new BCryptPasswordEncoder().encode("123456"));
		/*
		 generate the password, add the information to the user table, add user id and 
		 profile id to the usuarios_tem_perfis table to create the master user.
		 */

		SpringApplication.run(DemoSecurityApplication.class, args);
	}
	
	
	/*
	 *teste de email,  nessessario implements CommandLineRunner
	 * 
	 */
	/* 
	@Autowired
	JavaMailSender sender;
	
	@Override
	public void run(String... args) throws Exception {
		SimpleMailMessage simple = new SimpleMailMessage();
		simple.setTo("email@email.com");
		simple.setText("teste n1");
		simple.setSubject("teste 1");
		simple.setFrom("email@email.com"); // <--- THIS IS IMPORTANT
		simple.setSubject("email@email.com");
		sender.send(simple);
		System.out.println("email enviado!!!!!!!!!");
		
	}
	*/
	
}
