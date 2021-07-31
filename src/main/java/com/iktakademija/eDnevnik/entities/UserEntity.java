package com.iktakademija.eDnevnik.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;


@Entity 
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name="userType", discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "user") 
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity {

	@JsonView(Views.Public.class)
	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO) 
	@Column(nullable=false, name = "user_id")
	private Integer id;
	
	@JsonView(Views.Admin.class)
	@NotBlank
	@Column(nullable=false,name = "userName", unique = true)
	private String username;

	@NotBlank
	@Column(nullable=false, name = "password") 
	@JsonIgnore 
	private String password;
	
	@JsonView(Views.Admin.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY) 
	@JoinColumn(name = "role") 
	private RoleEntity role;
	
	@Version
	private Integer version;

	public UserEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

	public UserEntity(String username, String password, RoleEntity role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public RoleEntity getRole() {
		return role;
	}

	public void setRole(RoleEntity role) {
		this.role = role;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
