package com.mballem.curso.security.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.mballem.curso.security.domain.PerfilTipo;

//@EnableWebMvc
@Configuration
@EnableMethodSecurity//habilit anotation security
@EnableWebSecurity
public class SecurityConfig {
	
	private static final String ADMIN = PerfilTipo.ADMIN.getDesc();
	private static final String MEDICO = PerfilTipo.MEDICO.getDesc();
	private static final String PACIENTE = PerfilTipo.PACIENTE.getDesc();
	

	@Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests((authorize) -> authorize
                .requestMatchers(
                        antMatcher("/webjars/**"),
                        antMatcher("/css/**"),
                        antMatcher("/image/**"),
                        antMatcher("/js/**"),
                        antMatcher("/"),
                        antMatcher("/home"),
                        antMatcher("/expired"),
                        antMatcher("/u/novo/cadastro"),
                        antMatcher("/u/cadastro/realizado"),
                        antMatcher("/u/cadastro/paciente/salvar"),
                        antMatcher("/u/confirmacao/cadastro"),
                        antMatcher("/u/p/**")
                ).permitAll()
                .requestMatchers(
                        antMatcher("/u/editar/senha"),
                        antMatcher("/u/confirmar/senha")
                ).hasAnyAuthority(PACIENTE, MEDICO)
                .requestMatchers(
                        antMatcher("/u/**")
                ).hasAuthority(ADMIN)
                .requestMatchers(
                        antMatcher("/medicos/especialidade/titulo/*")
                ).hasAnyAuthority(PACIENTE, MEDICO)
                .requestMatchers(
                        antMatcher("/medicos/dados"),
                        antMatcher("/medicos/salvar"),
                        antMatcher("/medicos/editar")
                ).hasAnyAuthority(MEDICO, ADMIN)
                .requestMatchers(
                        antMatcher("/medicos/**")
                ).hasAuthority(MEDICO)
                .requestMatchers(
                        antMatcher("/pacientes/**")
                ).hasAuthority(PACIENTE)
                .requestMatchers(
                        antMatcher("/especialidades/datatables/server/medico/*")
                ).hasAnyAuthority(MEDICO, ADMIN)
                .requestMatchers(
                        antMatcher("/especialidades/titulo")
                ).hasAnyAuthority(MEDICO, ADMIN, PACIENTE)
                .requestMatchers(
                        antMatcher("/especialidades/**")
                ).hasAuthority(ADMIN)
                .anyRequest().authenticated()
        ).formLogin((formLogin) -> formLogin
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login-error")
                .permitAll()
        ).logout((logout) -> logout
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
        ).exceptionHandling((ex) -> ex
                .accessDeniedPage("/acesso-negado")
        ).rememberMe(
                withDefaults()
        ).sessionManagement((session) -> session
                .maximumSessions(1)
                .expiredUrl("/expired")
                .maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry())
        ).sessionManagement((session) -> session
                .sessionFixation()
                .newSession()
                .sessionAuthenticationStrategy(sessionAuthStrategy())
        );

        return http.build();
    }
	
	
    @Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	@Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		/*
        method responsible for managing the authentication referred to by the loadUserByUsername method of UsuarioService.
        */
        return authenticationConfiguration.getAuthenticationManager();
    }
	
	
	
	
	
	
	
	@Bean
	public SessionAuthenticationStrategy sessionAuthStrategy() {
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
