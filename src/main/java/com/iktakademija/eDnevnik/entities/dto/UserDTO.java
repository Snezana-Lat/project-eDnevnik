package com.iktakademija.eDnevnik.entities.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;

public class UserDTO {

	@JsonView(Views.Private.class)
	String name;
	@JsonView(Views.Private.class)
	String lastname;
	public UserDTO() {
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
	
	
}
