package com.mballem.curso.security.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

	// home
	@GetMapping({"/", "/home"})
	public String home() {
		return "home";
	}
	
	@GetMapping({"/login"})
	public String login() {
		return "login";
	}
	
	// login error
	@GetMapping({"/login-error"})
	public String loginError(ModelMap model, HttpServletRequest resp) {
		HttpSession session = resp.getSession();
		String lastException = String.valueOf(session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION"));
		if (lastException.contains(SessionAuthenticationException.class.getName())) {
			model.addAttribute("alerta", "erro");
			model.addAttribute("titulo", "Acesso rrecusado!");
			model.addAttribute("texto", "Você ja esta logado em outro dispositivo.");
			model.addAttribute("subTexto", "Faça logout ou espere a sessão expirar.");
			
			return "login";
		}
		
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais invãlidas!");
		model.addAttribute("texto", "Login ou senha incorretos, tente novamente.");
		model.addAttribute("subTexto", "acesso permitido apenas para cadastros já ativados.");
		
		return "login";
	}
	
	
		// login error
		@GetMapping({"/expired"})
		public String sessaoExpirada(ModelMap model) {
			/* used if SecurityConfig.class:
			 http.sessionManagement()
			 		.sessionFixation().newSession()
			 		.sessionAuthenticationStrategy(sessionAuthenticationStrategy());//set false -> .maxSessionsPreventsLogin(false)
			*/
			
			model.addAttribute("alerta", "erro");
			model.addAttribute("titulo", "Acesso recusado");
			model.addAttribute("texto", "Sua sessão expirou.");
			model.addAttribute("subTexto", "Você logou em outro dispositivo.");
			
			return "login";
		}
		
	
	// access denied
	@GetMapping({"/acesso-negado"})
	public String acessoNegado(ModelMap model, HttpServletResponse resp) {
		model.addAttribute("status", resp.getStatus());
		model.addAttribute("error", "Acesso Negado");
		model.addAttribute("message", "Você não tem acesso a esta área ou ação.");
		
		return "error";
	}
}
