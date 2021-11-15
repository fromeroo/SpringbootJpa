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
import com.springboot.app.jpa.models.entity.Categoria;
import com.springboot.app.jpa.models.service.IClienteService;

import com.springboot.app.jpa.util.paginator.PageRender;


@Controller
@SessionAttributes("categoria")
public class CategoriaController {
	
	
	@Autowired
	private IClienteService clienteServicio;
	
	
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
		
	
	@RequestMapping(value="listarCategoria", method = RequestMethod.GET)
	public String listar(@RequestParam(name="page", defaultValue = "0") int page,  Model model) {
				
		 Pageable pageRequest = PageRequest.of(page, 5);
		 
		 Page<Categoria> categorias = clienteServicio.findAllCategoria(pageRequest);
		 PageRender<Categoria> pageRender = new PageRender<>("/listarCategoria", categorias);
		
		model.addAttribute("titulo", "Listado de Categorias");
		model.addAttribute("categorias", categorias);
		model.addAttribute("page", pageRender);
		
		System.out.println(clienteServicio.findAllCategoria());
		return "listarCategoria";
	}
	
	
	@GetMapping(value="/formCategoria")
	public String crear(Map<String, Object> model) {
		
		Categoria categoria = new Categoria();
		model.put("categoria", categoria);
		model.put("titulo", "Formulario de Categoria");
		model.put("boton", "Crear");
		return "formCategorias";
		
	}
	
	@RequestMapping(value="/formCategoria", method = RequestMethod.POST)
	public String grabar(@Valid Categoria categoria, BindingResult result, Model model,SessionStatus status,RedirectAttributes flash ) {
		
		if(result.hasErrors()) {
			model.addAttribute("titulo", "Formulario de Categoria");
			return "formCategorias";
		}
		
		String mensajeFlash = (categoria.getId() != null)? "Categoria editada con Éxito" : "Categoria creada con Éxito";

		clienteServicio.save(categoria);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listarCategoria";
	}
	
	
	@GetMapping(value="/formCategoria/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String,Object> model, RedirectAttributes flash) {
		
		Categoria categoria = null;
		
		if (id > 0) {
			categoria = clienteServicio.findOneCategoria(id);
			if(categoria == null) {
				flash.addFlashAttribute("error", "La Categoria no existe en la BBDD");
			}
		} else {
			flash.addFlashAttribute("error", "El id no puede ser cero!");
			return "redirect:/listarCategoria";
		}
		
		model.put("titulo", "Editar Categoria");
		model.put("boton", "Editar");
		model.put("categoria", categoria);
		
		
		return "formCategorias";
	}
	
	
	@RequestMapping(value="/eliminarCategoria/{id}")
	public String eliminar(@PathVariable(value="id") Long id, RedirectAttributes flash) {
		
		if (id > 0) {
			Categoria categoria = clienteServicio.findOneCategoria(id);
			clienteServicio.deleteCategoria(id);
			flash.addFlashAttribute("success", "Categoria Eliminada con Éxito");
				
		}
		
		return "redirect:/listarCategoria";
	}
	
	
	

}
