	package com.rivera.empresa.eventos.controller;
	
	import java.util.List;
	import java.util.stream.Collectors;
	
	import org.springframework.security.core.Authentication;
	import org.springframework.security.core.GrantedAuthority;
	import org.springframework.security.core.context.SecurityContextHolder;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.ModelAttribute;
	
	@Controller
	public class MainController {
		
		@ModelAttribute
	    public void addAttributes(Model model) {
	        // Obtener el usuario autenticado
	        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	        String username = auth.getName();
	        List<String> roles = auth.getAuthorities().stream()
	                                  .map(GrantedAuthority::getAuthority)
	                                  .collect(Collectors.toList()); // Roles del usuario
	        
	        // Agregar al modelo
	        model.addAttribute("usuario", username);
	        model.addAttribute("roles", roles); // Lista de roles
	    }	
		
	
		@GetMapping("/")
	    public String home() {
	        return "index";
	    }
		
		@GetMapping("/eventos")
	    public String eventos() {
	        return "eventos";
	    }
		@GetMapping("/login")
	    public String login() {
	        return "login";
	    }
		
		@GetMapping("/usuarios")
	    public String usuarios() {
	        return "usuarios";
	    }
	}