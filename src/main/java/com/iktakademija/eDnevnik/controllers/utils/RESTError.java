package com.iktakademija.eDnevnik.controllers.utils;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;


public class RESTError {
	
	@JsonView(Views.Public.class)
	private Integer code;
	
	@JsonView(Views.Public.class)
	private String message;
	public RESTError() {
		super();
		// TODO Auto-generated constructor stub
	}
	public RESTError(Integer code, String message) {
		super();
		this.code = code;
		this.message = message;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
