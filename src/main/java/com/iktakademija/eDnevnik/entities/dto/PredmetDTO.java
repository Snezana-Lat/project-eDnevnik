package com.iktakademija.eDnevnik.entities.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;

public class PredmetDTO {

	@JsonView(Views.Admin.class)
	@NotBlank(message="Morate uneti naziv predmeta.")
	@Size(max=20, message="Naziv mora biti manji od {max}")
	private String naziv;

	@JsonView(Views.Admin.class)
	@NotNull(message="Morate uneti nedeljni broj casova.")
	@Min(value=1, message="Broj casova mora biti veci od 1.")
	@Max(value=10, message="Broj casova mora biti manji od 10.")
	private Integer nedeljniFondCasova;
	
	public PredmetDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Integer getNedeljniFondCasova() {
		return nedeljniFondCasova;
	}

	public void setNedeljniFondCasova(Integer nedeljniFondCasova) {
		this.nedeljniFondCasova = nedeljniFondCasova;
	}

	
	
}
