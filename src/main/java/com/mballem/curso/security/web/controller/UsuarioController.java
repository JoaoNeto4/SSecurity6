package com.mballem.curso.security.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mballem.curso.security.domain.Medico;
import com.mballem.curso.security.domain.Perfil;
import com.mballem.curso.security.domain.PerfilTipo;
import com.mballem.curso.security.domain.Usuario;
import com.mballem.curso.security.service.MedicoService;
import com.mballem.curso.security.service.UsuarioService;

@Controller
@RequestMapping("u")
public class UsuarioController {
	
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private MedicoService medicoService;

	// open user registration (doctor/admin/patient)
	@GetMapping("novo/cadastro/usuario")
	public String cadastroPorAdminParaAdminMedicoPaciente(Usuario usuario) {
		
		return "usuario/cadastro";
	}
	
	// open list of users
	@GetMapping("/lista")
	public String listarUsuarios() {
		
		return "usuario/lista";
	}
	
	// list users in datatables
	@GetMapping("/datatables/server/usuarios")
	public ResponseEntity<?> listarUsuariosDatatables(HttpServletRequest request) {
		
		return ResponseEntity.ok(service.buscarTodos(request));
	}
	
	
	@PostMapping("/cadastro/salvar")
	public String salvarUsuarios(Usuario usuario, RedirectAttributes attr) {
		/*
		 save user registration by administrator.
		 
		 It will not be used to register patients, as 
		 they will do so via the website before they are even logged in.
		 */
		
		List<Perfil> perfis = usuario.getPerfis();
		if (
				perfis.size() > 2 ||
				perfis.containsAll(Arrays.asList(new Perfil(1L), new Perfil(3L))) ||
				perfis.containsAll(Arrays.asList(new Perfil(2L), new Perfil(3L)))
				// id perfis : PerfilTipo.ADMIN.getCod() ... ...
		){
			attr.addFlashAttribute("falha", "Paciente nao pode ser Admin e/ou Médico.");
			attr.addFlashAttribute("usuario", usuario);
		} else {
			try {
				service.salvarUsuario(usuario);
				attr.addFlashAttribute("sucesso", "Operação realizada com sucesso!");
			} catch (DataIntegrityViolationException e) {
				attr.addFlashAttribute("falha", "Cadastro não realizado, email ja existente.");
			}
			
		}
		
		return "redirect:/u/novo/cadastro/usuario";
		//redirect to route method cadastroPorAdminParaAdminMedicoPaciente		
	}
	
	
	// pre-editing user registration
	@GetMapping("/editar/credenciais/usuario/{id}")
	public ModelAndView preEditarCredenciais(@PathVariable("id") Long id) {
		/*
			pre-editing because it will go through the saveUsers method, only 
			with the user id, thus encrypting the new password but keeping the same id.
		*/
		return new ModelAndView("/usuario/cadastro", "usuario", service.buscarPorId(id));
	}
	
	// pre-editing user registration
	@GetMapping("/editar/dados/usuario/{id}/perfis/{perfis}")
	public ModelAndView preEditarCadastroDadosPessoais(@PathVariable("id") Long usuarioId,
													   @PathVariable("perfis") Long[] perfisId) {
		
		
		Usuario us = service.buscarPorIdEPerfis(usuarioId, perfisId);
		
		if (us.getPerfis().contains(new Perfil(PerfilTipo.ADMIN.getCod())) &&
				!us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))
		) {
			return new ModelAndView("usuario/cadastro", "usuario", us);
		} else if (us.getPerfis().contains(new Perfil(PerfilTipo.MEDICO.getCod()))) {
			
			Medico medico = medicoService.buscarPorUsuarioId(usuarioId);
			return medico.hasNotId()
					? new ModelAndView("medico/cadastro", "medico", new Medico(new Usuario(usuarioId)))
					:  new ModelAndView("medico/cadastro", "medico", medico);
			
		} else if (us.getPerfis().contains(new Perfil(PerfilTipo.PACIENTE.getCod()))) {
			ModelAndView model = new ModelAndView("error");
			model.addObject("status", 403);
			model.addObject("erro", "Área Restrita");
			model.addObject("message", "Os dados de pacientes são restritos a ele.");
			 
			return model;
		}
		
		return new ModelAndView("redirect:/u/lista");
	}
	
	
	@GetMapping("/editar/senha")
	public String abrirEditarSenha() {
		return "usuario/editar-senha";
	}
		
	@PostMapping("/confirmar/senha")
	public String editarSenha(@RequestParam("senha1") String s1, @RequestParam("senha2") String s2,
							 @RequestParam("senha3") String s3, @AuthenticationPrincipal User user,
							 RedirectAttributes attr) {
		
		if (!s1.equals(s2)) {
			attr.addFlashAttribute("falha", "Senhas não conferem, tente novamente.");
			return "redirect:/u/editar/senha";
		}
		
		Usuario u = service.buscarPorEmail(user.getUsername());
		if(!UsuarioService.isSenhaCorreta(s3, u.getSenha())) {
			attr.addFlashAttribute("falha", "Senhas atual não confere, tente novamente.");
			return "redirect:/u/editar/senha";
		}
		
		service.alterarSenha(u, s1);
		attr.addFlashAttribute("sucesso", "Senhas alterada com sucesso.");
		return "redirect:/u/editar/senha";
	}
	
	@GetMapping("/novo/cadastro")
	public String novoCadastro(Usuario usuario) {
		
		return "cadastrar-se";
	}
	
	@GetMapping("/cadastro/realizado")
	public String cadastroRealizado() {
		
		return "fragments/mensagem";
	}
	
	@PostMapping("/cadastro/paciente/salvar")
	public String salvarCadastroPaciente(Usuario usuario, BindingResult result) throws MessagingException {
		try {
			service.salvarCadastroPaciente(usuario);
		} catch (DataIntegrityViolationException ex) {
			result.reject("email", "ops... Este email já existe na base de dados.");
			return "cadastrar-se";
		}
		
		return "redirect:/u/cadastro/realizado";
	}
	
	@GetMapping("/confirmacao/cadastro")
	public String respostaConfirmacaoCadastroPaciente(@RequestParam("codigo") String codigo, RedirectAttributes attr) {
		
		service.ativarCadastroPaciente(codigo);
		attr.addFlashAttribute("alerta", "sucesso");
		attr.addFlashAttribute("titulo", "Cadastro Ativado.");
		attr.addFlashAttribute("texto", "Parebéns, seu cadastro está ativo.");
		attr.addFlashAttribute("subtexto", "Siga com seu login/senha");
		
		return "redirect:/login";
	}
		
}
