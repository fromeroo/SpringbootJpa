package com.springboot.app.jpa.controllers;


import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.tomcat.jni.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.springboot.app.jpa.models.entity.Cliente;
import com.springboot.app.jpa.models.entity.Autor;
import com.springboot.app.jpa.models.service.IClienteService;

import com.springboot.app.jpa.util.paginator.PageRender;


@Controller
@SessionAttributes("autor")
public class AutorController {
	
	
	@Autowired
	private IClienteService clienteServicio;
	
	
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	
	@GetMapping(value="/uploadsAutor/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		Path pathFoto = Paths.get("uploads").resolve(filename).toAbsolutePath();
		
		Resource recurso = null;
		
		try {
			recurso =  new UrlResource(pathFoto.toUri());
			if(!recurso.exists() && !recurso.isReadable()) {
				throw new RuntimeException("Error:  La imagen no se pudo cargar" + pathFoto.getFileName());
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename()+ "\"")
				.body(recurso);
		
		
	} 
	
	
	
	@RequestMapping(value="listarAutor", method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue = "0") int page,  Model model) {
				
		 Pageable pageRequest = PageRequest.of(page, 5);
		 
		 Page<Autor> autores = clienteServicio.findAllAutor(pageRequest);
		 PageRender<Autor> pageRender = new PageRender<>("/listarAutor", autores);
		
		model.addAttribute("titulo", "Listado de autores");
		model.addAttribute("autores", autores);
		model.addAttribute("page", pageRender);
		
		System.out.println(clienteServicio.findAllAutor());
		return "listarAutor";
	}
	
	
	@GetMapping(value="/formAutor")
	public String crear(Map<String, Object> model) {
		
		Autor autor = new Autor();
		model.put("autor", autor);
		model.put("titulo", "Formulario de Autor");
		model.put("boton", "Crear");
		return "formAutores";
		
	}
	
	@RequestMapping(value="/formAutor", method = RequestMethod.POST)
	public String grabar(@Valid Autor autor, BindingResult result, Model model,@RequestParam("file") MultipartFile foto ,SessionStatus status,RedirectAttributes flash ) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Autor");
			return "formAutores";
		}
		
		
		if (!foto.isEmpty()) {
			
			String uniqueFileName = UUID.randomUUID().toString()+ "_" + foto.getOriginalFilename();
			
			Path rootPath = Paths.get("uploads").resolve(uniqueFileName);
			
			Path rootAbsolutePath = rootPath.toAbsolutePath();
		
			
			try {
				
				Files.copy(foto.getInputStream(), rootAbsolutePath);
				
				flash.addFlashAttribute("info", "Imagen subida correctamente");
				
				autor.setFoto(uniqueFileName);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		
		String mensajeFlash = (autor.getId() != null)? "Autor editado con Éxito" : "Autor creado con Éxito";

		clienteServicio.save(autor);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listarAutor";
	}
	
	
	@GetMapping(value="/formAutor/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String,Object> model, RedirectAttributes flash) {
		
		Autor autor = null;
		
		if (id > 0) {
			autor = clienteServicio.findOneAutor(id);
			if(autor == null) {
				flash.addFlashAttribute("error", "El Autor no existe en la BBDD");
			}
		} else {
			flash.addFlashAttribute("error", "El id no puede ser cero!");
			return "redirect:/listarAutor";
		}
		
		model.put("titulo", "Editar Autor");
		model.put("boton", "Editar");
		model.put("autor", autor);
		
		
		return "formAutores";
	}
	
	
	@RequestMapping(value="/eliminarAutor/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		
		if (id > 0) {
			Autor autor = clienteServicio.findOneAutor(id);
			clienteServicio.deleteAutor(id);
			flash.addFlashAttribute("success", "Autor Eliminado con Éxito");
			
			Path rootPath = Paths.get("uploads").resolve(autor.getFoto()).toAbsolutePath();
			java.io.File archivo = rootPath.toFile();	
			
			if(archivo.exists() && archivo.canRead()) {
				
				if(archivo.delete()) {
					flash.addFlashAttribute("info","Archivo eliminado "+ autor.getFoto() );
				}
			}
				
		}
		return "redirect:/listarAutor";
	}
	
	
	@GetMapping(value="/verAutor/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Autor autor =  clienteServicio.findOneAutor(id);   //clienteServicio.findOne(id);
		if (autor == null) {
			flash.addFlashAttribute("error", "Autor no existe en la BD");
			return "redirect:/listarAutor";
		}
		
		model.put("autor", autor);
		model.put("titulo", "Detalles del autor: " + autor.getNombre());
		
		return "verAutor";
		
	}
	
	

}
