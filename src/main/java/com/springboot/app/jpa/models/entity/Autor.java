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
@Table(name="autores")
public class Autor  implements Serializable{
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Pattern(regexp = "[a-zA-Z]+")
	@Size( min = 4)
	private String nombre;
	
	@Pattern(regexp = "[a-zA-Z]+")
	@Size( min = 4)
	private String apellido;
	
	@NotBlank
	private String nacionalidad;
	
	
	@Pattern(regexp = "[a-zA-Z]+")
	@Size( min = 3)
	@NotBlank
	private String seudonimo;
	
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
	@Column(name="create_at")
	private Date createAt;
	
	
	private String foto;

	@OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Factura> facturas;
	
	
	public Autor() {
		

	}
	
	public Autor(Long id, String nombre, String apellido, String nacionalidad, String seudonimo, Date fecha_nacimiento, Date createAt) {
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.nacionalidad = nacionalidad;
		this.seudonimo = seudonimo;
		this.createAt = createAt;
		
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getSeudonimo() {
		return seudonimo;
	}

	public void setSeudonimo(String seudonimo) {
		this.seudonimo = seudonimo;
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

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	private static final long serialVersionUID = 1L;
}
