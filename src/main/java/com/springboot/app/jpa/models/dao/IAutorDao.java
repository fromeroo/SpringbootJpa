package com.springboot.app.jpa.models.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.springboot.app.jpa.models.entity.Autor;

public interface IAutorDao  extends PagingAndSortingRepository<Autor, Long>{
	
	
	@Query("select a from Autor a")
	public Autor fetchByIdWhitFacturas(Long id);
	
	
	

}
