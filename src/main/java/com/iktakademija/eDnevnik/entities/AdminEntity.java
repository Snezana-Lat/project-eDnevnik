package com.iktakademija.eDnevnik.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.validation.constraints.NotBlank;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "admin")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class AdminEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@NotBlank
	@Column(nullable = false)
	private String name;
	
	//@Version
	//private Integer version;

	public AdminEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
