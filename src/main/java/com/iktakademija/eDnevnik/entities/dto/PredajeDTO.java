package com.iktakademija.eDnevnik.entities.dto;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;

public class PredajeDTO {

	
	@JsonView(Views.Admin.class)
	@NotNull(message ="Morate uneti id predmeta.")
	Integer predmetId;
	
	@NotNull(message ="Morate uneti id nastavnika.")
	@JsonView(Views.Admin.class)
	Integer nastavnikId;
	
	@NotNull(message ="Morate uneti id odeljenja.")
	@JsonView(Views.Admin.class)
	Integer odeljenjeId;
	
	public PredajeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getPredmetId() {
		return predmetId;
	}

	public void setPredmetId(Integer predmetId) {
		this.predmetId = predmetId;
	}

	public Integer getNastavnikId() {
		return nastavnikId;
	}

	public void setNastavnikId(Integer nastavnikId) {
		this.nastavnikId = nastavnikId;
	}

	public Integer getOdeljenjeId() {
		return odeljenjeId;
	}

	public void setOdeljenjeId(Integer odeljenjeId) {
		this.odeljenjeId = odeljenjeId;
	}
	
	
}
