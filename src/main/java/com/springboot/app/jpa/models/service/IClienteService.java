package com.springboot.app.jpa.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springboot.app.jpa.models.entity.Cliente;
import com.springboot.app.jpa.models.entity.Libro;
import com.springboot.app.jpa.models.entity.Factura;
import com.springboot.app.jpa.models.entity.Producto;
import com.springboot.app.jpa.models.entity.Categoria;

public interface IClienteService {
	
	
	public Page<Cliente> findAll(Pageable pageable);
	 
	public List<Cliente> findAll();
	
	public void save(Cliente cliente);
	
	public Cliente findOne(Long id);
	
	public void delete(Long id);
	
	public Page<Libro> findAllLibro(Pageable pageable);
	 
	public List<Libro> findAllLibro();
	
	public void save(Libro libro);
	
	public Libro findOneLibro(Long id);
	
	public void deleteLibro(Long id);
	
	public Page<Categoria> findAllCategoria(Pageable pageable);
	 
	public List<Categoria> findAllCategoria();
	
	public void save(Categoria categoria);
	
	public Categoria findOneCategoria(Long id);
	
	public void deleteCategoria(Long id);
	
	public Cliente fetchByIdWhitFacturas(Long id);
	
	public List<Producto> findByNombre(String term);
	
	public void saveFactura(Factura factura);
	
	public Producto findProductoById(Long id);
	
	public Factura findFacturaById(Long id);
	
	public void deleteFactura(Long id);
	
	public Factura fetchFacturaByIdWhitClienteWhitDetalleFacturaWhitProducto(Long id);

}
