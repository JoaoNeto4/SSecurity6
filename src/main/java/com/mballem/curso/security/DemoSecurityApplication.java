package com.mballem.curso.security;


//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import com.mballem.curso.security.service.EmailService;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
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
	
	@Autowired
	EmailService service;
	
	@Override
	public void run(String... args) throws Exception {
		// apenas para finalidade de testes
		service.enviarPedidoDeConfirmacaoDeCadastro("jneto_1011@outlook.com", "cod4551");
		
	}
	*/
	
	
	/*
	@Override
	public void run(String... args) throws Exception {

	}

	@PostConstruct
	public void init(){
		// Setting Spring Boot SetTimeZone
		TimeZone.setDefault(TimeZone.getTimeZone("America/Recife"));
	}
	*/
	
}
