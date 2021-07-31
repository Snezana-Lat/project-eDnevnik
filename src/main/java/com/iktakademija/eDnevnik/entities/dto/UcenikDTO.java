package com.iktakademija.eDnevnik.entities.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UcenikDTO {

	@NotBlank(message = "Morate uneti ime")
	@Size(min = 2, max = 30, message = "Ime mora imati izmedju {min} i {max} slova.")
	private String name;

	@NotBlank(message = "Morate uneti prezime")
	@Size(min = 2, max = 30, message = "Ime mora imati izmedju {min} i {max} slova.")
	private String lastname;

	@NotBlank(message = "Morate uneti JMBG.")
	@Size(min = 13, max = 13, message = "Jmbg mora imati 13 cifara.")
	private String jmbg;

	public UcenikDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

}
