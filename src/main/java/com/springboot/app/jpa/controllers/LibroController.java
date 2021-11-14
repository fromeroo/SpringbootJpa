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
import com.springboot.app.jpa.models.entity.Libro;
import com.springboot.app.jpa.models.service.IClienteService;

import com.springboot.app.jpa.util.paginator.PageRender;


@Controller
@SessionAttributes("libro")
public class LibroController {
	
	
	@Autowired
	private IClienteService clienteServicio;
	
	
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	
	
	@GetMapping(value="/uploadsLibro/{filename:.+}")
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
	
	
	
	@RequestMapping(value="listarLibro", method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue = "0") int page,  Model model) {
				
		 Pageable pageRequest = PageRequest.of(page, 5);
		 
		 Page<Libro> libros = clienteServicio.findAllLibro(pageRequest);
		 PageRender<Libro> pageRender = new PageRender<>("/listarLibro", libros);
		
		model.addAttribute("titulo", "Listado de libros");
		model.addAttribute("libros", libros);
		model.addAttribute("page", pageRender);
		
		System.out.println(clienteServicio.findAllLibro());
		return "listarLibro";
	}
	
	
	@GetMapping(value="/formLibro")
	public String crear(Map<String, Object> model) {
		
		Libro libro = new Libro();
		model.put("libro", libro);
		model.put("titulo", "Formulario de Libro");
		model.put("boton", "Crear");
		return "formLibros";
		
	}
	
	@RequestMapping(value="/formLibro", method = RequestMethod.POST)
	public String grabar(@Valid Libro libro, BindingResult result, Model model,@RequestParam("file") MultipartFile foto ,SessionStatus status,RedirectAttributes flash ) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Libro");
			return "formLibros";
		}
		
		
		if (!foto.isEmpty()) {
			
			String uniqueFileName = UUID.randomUUID().toString()+ "_" + foto.getOriginalFilename();
			
			Path rootPath = Paths.get("uploads").resolve(uniqueFileName);
			
			Path rootAbsolutePath = rootPath.toAbsolutePath();
		
			
			try {
				
				Files.copy(foto.getInputStream(), rootAbsolutePath);
				
				flash.addFlashAttribute("info", "Imagen subida correctamente");
				
				libro.setFoto(uniqueFileName);
				
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
		}
		
		String mensajeFlash = (libro.getId() != null)? "Libro editado con Éxito" : "Libro creado con Éxito";

		clienteServicio.save(libro);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listarLibro";
	}
	
	
	@GetMapping(value="/formLibro/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String,Object> model, RedirectAttributes flash) {
		
		Libro libro = null;
		
		if (id > 0) {
			libro = clienteServicio.findOneLibro(id);
			if(libro == null) {
				flash.addFlashAttribute("error", "El Libro no existe en la BBDD");
			}
		} else {
			flash.addFlashAttribute("error", "El id no puede ser cero!");
			return "redirect:/listarLibro";
		}
		
		model.put("titulo", "Editar Libro");
		model.put("boton", "Editar");
		model.put("libro", libro);
		
		
		return "formLibros";
	}
	
	
	@RequestMapping(value="/eliminarLibro/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		
		if (id > 0) {
			Libro libro = clienteServicio.findOneLibro(id);
			clienteServicio.deleteLibro(id);
			flash.addFlashAttribute("success", "Libro Eliminado con Éxito");
			
			Path rootPath = Paths.get("uploads").resolve(libro.getFoto()).toAbsolutePath();
			java.io.File archivo = rootPath.toFile();	
			
			if(archivo.exists() && archivo.canRead()) {
				
				if(archivo.delete()) {
					flash.addFlashAttribute("info","Archivo eliminado "+ libro.getFoto() );
				}
			}
				
		}
		return "redirect:/listarLibro";
	}
	
	
	@GetMapping(value="/verLibro/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash) {
		
		Libro libro =  clienteServicio.findOneLibro(id);   //clienteServicio.findOne(id);
		if (libro == null) {
			flash.addFlashAttribute("error", "Libro no existe en la BD");
			return "redirect:/listarLibro";
		}
		
		model.put("libro", libro);
		model.put("titulo", "Detalles del libro: " + libro.getTitulo());
		
		return "verLibro";
		
	}
	
	

}
