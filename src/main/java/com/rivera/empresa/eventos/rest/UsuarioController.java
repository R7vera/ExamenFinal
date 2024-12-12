package com.rivera.empresa.eventos.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.rivera.empresa.eventos.converter.UsuarioConverter;
import com.rivera.empresa.eventos.dto.UsuarioDto;
import com.rivera.empresa.eventos.entity.Usuario;
import com.rivera.empresa.eventos.service.PdfService;
import com.rivera.empresa.eventos.service.UsuarioService;
import com.rivera.empresa.eventos.util.WrapperResponse;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private UsuarioConverter converter;
	
	@Autowired
	private PdfService pdfService;
	
	@GetMapping
	public ResponseEntity<List<UsuarioDto>> findAll(){				
		List<UsuarioDto> registros = converter.fromEntity(service.findAll());		
		return new WrapperResponse(true, "success", registros).createResponse(HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDto> create (@RequestBody Usuario usuario){
		UsuarioDto registro = converter.fromEntity(service.save(usuario));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
	}
	
	
	@PutMapping("/{id}/deactivate")
	public ResponseEntity<UsuarioDto> deactivate(@PathVariable("id") int id){
		UsuarioDto registro = converter.fromEntity(service.deactivate(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	@PutMapping("/{id}/activate")
	public ResponseEntity<UsuarioDto> activate(@PathVariable("id") int id){
		UsuarioDto registro=converter.fromEntity(service.activate(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDto> update(@PathVariable("id") int id,@RequestBody Usuario usuario){
		UsuarioDto registro = converter.fromEntity(service.save(usuario));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> findById(@PathVariable("id") int id){
		UsuarioDto registro=converter.fromEntity(service.findById(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	
	

	@GetMapping("/report")
	public ResponseEntity<byte[]> generateUsuarioReport() {
	    // Obtener lista de usuarios
	    List<UsuarioDto> usuarios = converter.fromEntity(service.findAll());
	    
	    // Obtener fecha y hora actual
	    LocalDateTime fecha = LocalDateTime.now();
	    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    String fechaHora = fecha.format(formato);
	    
	    // Contexto para Thymeleaf
	    Context context = new Context();
	    context.setVariable("registros", usuarios); // Usuarios en lugar de categorías
	    context.setVariable("fecha", fechaHora); // Fecha de generación
	    
	    // Crear el PDF a partir de la plantilla
	    byte[] pdfBytes = pdfService.createPdf("usuarioReport", context); // Cambia el nombre de la plantilla a 'usuarioReport'
	    
	    // Configurar encabezados HTTP para respuesta PDF
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_PDF);
	    headers.setContentDispositionFormData("inline", "reporte_usuarios.pdf");
	    
	    return ResponseEntity.ok().headers(headers).body(pdfBytes);
	}

}
