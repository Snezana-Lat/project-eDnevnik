package com.iktakademija.eDnevnik.entities.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;

public class OdeljenjeDTO {

	@JsonView(Views.Admin.class)
	@NotNull(message="Morate uneti broj razreda.")
	@Min(value=1, message="Razred mora biti od 1 do 8.")
	@Max(value=8, message="Razred mora biti od 1 do 8.")
	private Integer razred;
	
	@JsonView(Views.Admin.class)
	@NotNull(message="Morate uneti broj odeljenja.")
	@Min(value=1, message="Oznaka mora biti od 1 do 5.")
	@Max(value=5, message="Oznaka mora biti od 1 do 5.")
	private Integer oznakaOdeljenja;
	
	
	public OdeljenjeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getRazred() {
		return razred;
	}
	public void setRazred(Integer razred) {
		this.razred = razred;
	}
	public Integer getOznakaOdeljenja() {
		return oznakaOdeljenja;
	}
	public void setOznakaOdeljenja(Integer oznakaOdeljenja) {
		this.oznakaOdeljenja = oznakaOdeljenja;
	}
	
	
}
