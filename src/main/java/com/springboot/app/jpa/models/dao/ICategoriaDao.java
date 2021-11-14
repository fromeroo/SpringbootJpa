package com.springboot.app.jpa.models.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.springboot.app.jpa.models.entity.Categoria;

public interface ICategoriaDao  extends PagingAndSortingRepository<Categoria, Long>{
	
	
	@Query("select c from Categoria c")
	public Categoria fetchByIdWhitFacturas(Long id);
	
	
	

}