package com.iktakademija.eDnevnik.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;


@Entity
@Table(name = "roditelj")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class RoditeljEntity extends UserEntity {

	@JsonView(Views.Public.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@JsonView(Views.Private.class)
	@NotBlank(message="Morate uneti ime.")
	@Column(nullable = false)
	private String name;
	
	@JsonView(Views.Private.class)
	@NotBlank(message="Morate uneti jedinstveni JMBG od 13 cifara.")
	@Column(nullable=false,unique = true)
	@Size(min = 13, max = 13)
	private String jmbg;
	
	@JsonView(Views.Private.class)
	@NotBlank(message="Morate uneti prezime.")
	@Column(nullable=false)
	private String lastname;
	
	@JsonView(Views.Private.class)
	@Column(nullable=false)
	@NotBlank(message="Morate uneti email.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+
			"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email is not valid.")
	private String email;
	
	@JsonView(Views.Private.class)
	@OneToMany(mappedBy="roditelj", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonManagedReference(value="roditeljUcenik")
	private List<UcenikEntity> deca;
	
	
	
	public RoditeljEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public RoditeljEntity(String username, String password, RoleEntity role) {
		super(username, password, role);
		// TODO Auto-generated constructor stub
	}

	public RoditeljEntity( String name, String lastname, String email) {
		
		this.name = name;
		this.lastname = lastname;
		this.email = email;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<UcenikEntity> getDeca() {
		return deca;
	}

	public void setDeca(List<UcenikEntity> deca) {
		this.deca = deca;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}
	
	
	
}
