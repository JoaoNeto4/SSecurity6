package com.mballem.curso.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.service.UsuarioService;

@EnableGlobalMethodSecurity(prePostEnabled = true)//habilit anotation security
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();
	
	@Autowired
	private UsuarioService service;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers("/webjars/**", "/css/**", "/image/**", "/js/**").permitAll()
			.antMatchers("/", "/home", "/expired").permitAll()
			.antMatchers("/u/novo/cadastro", "/u/cadastro/realizado", "/u/cadastro/paciente/salvar").permitAll()
			.antMatchers("/u/confirmacao/cadastro").permitAll()
			.antMatchers("/u/p/**").permitAll()
			
			// private access admin
			.antMatchers("/u/editar/senha", "/u/confirmar/senha").hasAnyAuthority(MEDICO, PACIENTE)
			.antMatchers("/u/**").hasAnyAuthority(ADMIN)
			
			
			// private access medic
			.antMatchers("/medicos/especialidade/titulo/*").hasAnyAuthority(PACIENTE, MEDICO)
			.antMatchers("/medicos/dados**", "/medicos/salvar**", "/medicos/editar**").hasAnyAuthority(MEDICO, ADMIN)
			.antMatchers("/medicos/**").hasAnyAuthority(MEDICO)
			
			// private access patiente
			.antMatchers("/pacientes/**").hasAnyAuthority(PACIENTE, ADMIN)
			
			// private access specialties
			.antMatchers("/especialidades/datatables/server/medico/*").hasAnyAuthority(MEDICO, ADMIN)
			.antMatchers("/especialidades/titulo").hasAnyAuthority(MEDICO, ADMIN, PACIENTE)
			.antMatchers("/especialidades/**").hasAnyAuthority(ADMIN)
			
			
			.anyRequest().authenticated()
			.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/", true)
				.failureUrl("/login-error")
				.permitAll()
			.and()
				.logout()
				.logoutSuccessUrl("/")
			.and()
				.exceptionHandling()
				.accessDeniedPage("/acesso-negado")
			.and()
				.rememberMe();/* remember-me for login page*/
		
		
		http.sessionManagement()//gerenciamento de sessão
				.maximumSessions(1)//max sessions
				.expiredUrl("/expired")
				.maxSessionsPreventsLogin(true)//permite login em um segundo dispositivo?
				.sessionRegistry(sessionRegistry());
		
		//or
		/* 
		 http.sessionManagement()
		 		.sessionFixation().newSession()
		 		.sessionAuthenticationStrategy(sessionAuthenticationStrategy());//set false -> .maxSessionsPreventsLogin(false)
		*/
				
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		/*
		 method responsible for managing the authentication referred to by the loadUserByUsername method of UsuarioService.
		 */
		
		auth.userDetailsService(service).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	
	
	
	
	
	
	@Bean
	public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
		return new RegisterSessionAuthenticationStrategy(sessionRegistry());
	}
	
	
	
	@Bean
	public SessionRegistry sessionRegistry() {
		/*
		 * responsavel por passar configuração de sessao a cada login utilizado na sessão
		 */
		return new SessionRegistryImpl();
	}
	
	@Bean
	public ServletListenerRegistrationBean<?> servletListenerRegistrationBean() {
		/*
		 * registra os logins no servlet da applicação
		 */
		return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
	}
	

	
}
