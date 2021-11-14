package com.springboot.app.jpa.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name="libros")
public class Libro  implements Serializable{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty
	@NotBlank(message = "No debe contener Espacios")
	@Size(min = 5 , max = 65 )
	private String titulo;
	
	@Pattern(regexp = "[a-zA-Z]+")
	private String descripcion;
	
	@NotNull
	private Integer anno;
	
	@NotEmpty
	@Pattern(regexp = "^(?=(?:\\D*\\d){10}(?:(?:\\D*\\d){3})?$)[\\d-]+$")
	private String isbn;
	
	@NotNull
	@Min(value = 0)
	private Integer cantidad_paginas;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	@NotNull
	private Integer precio;
	
	private String foto;

	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Factura> facturas;
	
	
	public Libro() {
		

	}
	
	public Libro(Long id, String titulo, String descripcion, Integer anno, String isbn, Integer cantidad_paginas, Integer precio, Date createAt) {
		this.id = id;
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.anno = anno;
		this.isbn = isbn;
		this.cantidad_paginas = cantidad_paginas;
		this.precio= precio;
		this.createAt = createAt;
		
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}	

	public Integer getAnno() {
		return anno;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public void setAnno(Integer anno) {
		this.anno = anno;
	}
	

	public Integer getCantidad_paginas() {
		return cantidad_paginas;
	}



	public void setCantidad_paginas(Integer cantidad_paginas) {
		this.cantidad_paginas = cantidad_paginas;
	}


	public Date getCreateAt() {
		return createAt;
	}




	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}



	public String getFoto() {
		return foto;
	}


	public void setFoto(String foto) {
		this.foto = foto;
	}

	public Integer getPrecio() {
		return precio;
	}



	public void setPrecio(Integer precio) {
		this.precio = precio;
	}

	
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	private static final long serialVersionUID = 1L;
}
