package com.springboot.app.jpa.models.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.springboot.app.jpa.models.entity.Libro;

public interface ILibroDao  extends PagingAndSortingRepository<Libro, Long>{
	
	
	@Query("select l from Libro l")
	public Libro fetchByIdWhitFacturas(Long id);
	
	
	

}
