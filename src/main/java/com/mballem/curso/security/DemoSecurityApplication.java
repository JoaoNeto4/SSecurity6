package com.mballem.curso.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
}
