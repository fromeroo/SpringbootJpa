package com.springboot.app.jpa.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.app.jpa.models.dao.IClienteDao;
import com.springboot.app.jpa.models.dao.ILibroDao;
import com.springboot.app.jpa.models.dao.ICategoriaDao;
import com.springboot.app.jpa.models.dao.IFacturaDao;
import com.springboot.app.jpa.models.dao.IProductoDao;
import com.springboot.app.jpa.models.entity.Cliente;
import com.springboot.app.jpa.models.entity.Libro;
import com.springboot.app.jpa.models.entity.Categoria;
import com.springboot.app.jpa.models.entity.Factura;
import com.springboot.app.jpa.models.entity.Producto;


@Service
public class ClienteServiceImpl  implements IClienteService {
	
	
	@Autowired
	private IClienteDao clienteDao;
	
	@Autowired
	private IProductoDao productoDao;
	
	@Autowired
	private IFacturaDao facturaDao;
	
	@Autowired
	private ILibroDao libroDao;

	@Autowired
	private ICategoriaDao categoriaDao;
	
	@Transactional(readOnly = true)
	@Override
	public List<Cliente> findAll() {
		
		return (List<Cliente>) clienteDao.findAll();
	}

	@Transactional
	@Override
	public void save(Cliente cliente) {
		
		clienteDao.save(cliente);
		
	}

	@Transactional(readOnly = true)
	@Override
	public Cliente findOne(Long id) {
		
		return clienteDao.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void delete(Long id) {
		clienteDao.deleteById(id);
		
	}
	
	  @Transactional(readOnly = true)
	  @Override 
	  public Page<Cliente> findAll(Pageable pageable) {
	  
	  return clienteDao.findAll(pageable); }

	@Transactional(readOnly = true)  
	@Override
	public List<Producto> findByNombre(String term) {
		// TODO Auto-generated method stub
		return productoDao.findByNombre(term);
	}

	@Transactional()
	@Override
	public void saveFactura(Factura factura) {
		
		facturaDao.save(factura);
	
		
	}

	@Transactional(readOnly = true) 
	@Override
	public Producto findProductoById(Long id) {
	
		return productoDao.findById(id).orElse(null);
	}

	@Transactional(readOnly = true) 
	@Override
	public Factura findFacturaById(Long id) {
		
		return facturaDao.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void deleteFactura(Long id) {
		
		facturaDao.deleteById(id);
		
	}

	@Transactional(readOnly = true) 
	@Override
	public Factura fetchFacturaByIdWhitClienteWhitDetalleFacturaWhitProducto(Long id) {
		return facturaDao.fetchByIdWhitClienteWhitDetalleFacturaWhitProducto(id);
	}

	@Transactional(readOnly = true) 
	@Override
	public Cliente fetchByIdWhitFacturas(Long id) {
		
		return clienteDao.fetchByIdWhitFacturas(id);
	}
	
	
	@Transactional(readOnly = true)
	@Override
	public List<Libro> findAllLibro() {
		
		return (List<Libro>) libroDao.findAll();
	}

	@Transactional
	@Override
	public void save(Libro libro) {
		
		libroDao.save(libro);
		
	}

	@Transactional(readOnly = true)
	@Override
	public Libro findOneLibro(Long id) {
		
		return libroDao.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void deleteLibro(Long id) {
		libroDao.deleteById(id);
		
	}
	
	  @Transactional(readOnly = true)
	  @Override 
	  public Page<Libro> findAllLibro(Pageable pageable) {
	  
	  return libroDao.findAll(pageable); }
	  
	  
	  
	  	@Transactional(readOnly = true)
		@Override
		public List<Categoria> findAllCategoria() {
			
			return (List<Categoria>) categoriaDao.findAll();
		}

		@Transactional
		@Override
		public void save(Categoria categoria) {
			
			categoriaDao.save(categoria);
			
		}

		@Transactional(readOnly = true)
		@Override
		public Categoria findOneCategoria(Long id) {
			
			return categoriaDao.findById(id).orElse(null);
		}


		@Transactional
		@Override
		public void deleteCategoria(Long id) {
			categoriaDao.deleteById(id);
			
		}
		
		  @Transactional(readOnly = true)
		  @Override 
		  public Page<Categoria> findAllCategoria(Pageable pageable) {
		  
		  return categoriaDao.findAll(pageable); }
	
	 

}
