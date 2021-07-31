package com.iktakademija.eDnevnik.entities.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RoditeljDTO {

	@NotBlank(message="Morate uneti ime.")
	@Size(min=2, max=30, message = "Ime mora imati izmedju {min} i {max} slova.")
	private String name;
	
	@NotBlank(message="Morate uneti jedinstveni JMBG od 13 cifara.")
	@Size(min = 13, max = 13)
	private String jmbg;
	
	@NotBlank(message="Morate uneti prezime.")
	@Size(min=2, max=30, message = "Ime mora imati izmedju {min} i {max} slova.")
	private String lastname;
	
	
	@NotBlank(message="Morate uneti email.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+
			"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email is not valid.")
	private String email;

	public RoditeljDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
