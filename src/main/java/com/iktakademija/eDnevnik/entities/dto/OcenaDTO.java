package com.iktakademija.eDnevnik.entities.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.iktakademija.eDnevnik.enums.EOcena;

public class OcenaDTO {

	@NotNull(message = "Morate uneti ocenu od 1 do 5.")
	@Min(value=1, message="Ocena mora biti od 1 do 5!")
	@Max(value=5, message="Ocena mora biti od 1 do 5!")
	private Integer ocena;
	
	
	@Enumerated(EnumType.STRING)
//	@NotBlank(message = "Unesite oznaku ocene.")
	private EOcena  oznaka;
	
	


	public OcenaDTO() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getOcena() {
		return ocena;
	}


	public void setOcena(Integer ocena) {
		this.ocena = ocena;
	}


	public EOcena getOznaka() {
		return oznaka;
	}


	public void setOznaka(EOcena oznaka) {
		this.oznaka = oznaka;
	}
	
	
}
