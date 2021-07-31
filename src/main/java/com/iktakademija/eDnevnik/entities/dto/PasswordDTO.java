package com.iktakademija.eDnevnik.entities.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class PasswordDTO {

	@NotBlank(message="Morate uneti stari password!")
	String oldPass;
	
	@Pattern(regexp ="^.*(?=.{5,})(?=.*\\d)(?=.*[a-zA-Z]).*$", message="Password nije validan.\n Mora imati vise od 5 karaktera i mora sadrzati bar jedan broj")
	@NotBlank(message="Morate uneti novi password.")
	String newPass;

	public PasswordDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getOldPass() {
		return oldPass;
	}

	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}
	
	
}
