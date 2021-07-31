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
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;



@Entity
@Table(name = "nastavnik")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class NastavnikEntity extends UserEntity {

	@JsonView(Views.Public.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@JsonView(Views.Public.class)
	@NotBlank(message = "Morate uneti ime.")
	@Column(nullable = false)
	private String name;
	
	@JsonView(Views.Public.class)
	@NotBlank(message = "Morate uneti prezime.")
	@Column(nullable=false )
	private String lastname;
	
	@JsonView(Views.Private.class)
	@NotBlank(message = "Morate uneti JMBG.")
	@Column(nullable=false,unique = true)
	@Size(min = 13, max = 13, message ="JMBG mora biti jedinstven i imati 13 cifara")
	private String jmbg;
	
	@JsonView(Views.Private.class)
	@NotBlank(message = "Morate uneti jedinstveni broj licence.")
	@Column(nullable=false,unique = true)
	private String brLicence;
	
	@JsonView(Views.Private.class)
	@OneToMany(mappedBy="nastavnik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//	@JsonManagedReference(value="nastavnikPredaje")
	@JsonIgnore
	private List<PredajeEntity> predaje;
	
	@JsonView(Views.Private.class)
	@OneToMany(mappedBy = "nastavnik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//	@JsonManagedReference(value = "ocenaNastavnik")
	@JsonIgnore
	private List<OcenaEntity> ocene;

	@JsonView(Views.Private.class)
	@OneToMany(mappedBy="nastavnik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	//@JsonManagedReference(value="nastavnikPredmet")
	@JsonIgnore
	List<NastavnikPredmet> predmeti;
	
	public NastavnikEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NastavnikEntity(String username, String password, RoleEntity role) {
		super(username, password, role);
		// TODO Auto-generated constructor stub
	}
	public NastavnikEntity(String name, String lastname) {
		this.name = name;
		this.lastname = lastname;
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
	public String getBrLicence() {
		return brLicence;
	}
	public void setBrLicence(String brLicence) {
		this.brLicence = brLicence;
	}
	public List<PredajeEntity> getPredaje() {
		return predaje;
	}
	public void setPredaje(List<PredajeEntity> predaje) {
		this.predaje = predaje;
	}
	public String getJmbg() {
		return jmbg;
	}
	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}
	public List<OcenaEntity> getOcene() {
		return ocene;
	}
	public void setOcene(List<OcenaEntity> ocene) {
		this.ocene = ocene;
	}
	public List<NastavnikPredmet> getPredmeti() {
		return predmeti;
	}
	public void setPredmeti(List<NastavnikPredmet> predmeti) {
		this.predmeti = predmeti;
	}
	
	
	
}
