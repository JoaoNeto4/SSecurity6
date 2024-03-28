package com.mballem.curso.security.web.controller;

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
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais invãlidas!");
		model.addAttribute("texto", "Login ou senha incorretos, tente novamente.");
		model.addAttribute("subTexto", "acesso permitido apenas para cadastros já ativados.");
		
		return "login";
	}
}
