package com.iktakademija.eDnevnik.entities.dto;



import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;

public class PredajeDTOUpdate {
	@JsonView(Views.Admin.class)
	Integer predmetId;
	
	@JsonView(Views.Admin.class)
	Integer nastavnikId;
	
	@JsonView(Views.Admin.class)
	Integer odeljenjeId;
	
	public PredajeDTOUpdate() {
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
