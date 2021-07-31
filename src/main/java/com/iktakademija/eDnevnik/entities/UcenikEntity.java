package com.iktakademija.eDnevnik.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;

@Entity
@Table(name = "ucenik")
@JsonIgnoreProperties({ "handler", "hibernateLazyInitializer" })
public class UcenikEntity extends UserEntity{

	@JsonView(Views.Public.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@JsonView(Views.Private.class)
	@NotBlank
	@Column(nullable = false)
	private String name;
	
	@JsonView(Views.Private.class)
	@NotBlank
	@Column(nullable=false)
	private String lastname;
	
	@JsonView(Views.Private.class)
	@NotBlank
	@Column(nullable=false,unique = true)
	@Size(min = 13, max = 13)
	private String jmbg;
	
	@JsonView(Views.Private.class)
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="roditelj")
	@JsonBackReference(value="roditeljUcenik")
	private RoditeljEntity roditelj;
	
	@JsonView(Views.Private.class)
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="odeljenje")
	@JsonBackReference(value="odeljenjeUcenik")
	private OdeljenjeEntity odeljenje;
	
	@JsonView(Views.Private.class)
	@OneToMany(mappedBy="ucenik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	//@JsonManagedReference(value="ucenikOcena")
	@JsonIgnore
	private List<OcenaEntity> ocene;
	
	
	
	
	public UcenikEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public UcenikEntity( String name, String lastname) {
		super();
	
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
	public RoditeljEntity getRoditelj() {
		return roditelj;
	}
	public void setRoditelj(RoditeljEntity roditelj) {
		this.roditelj = roditelj;
	}
	public OdeljenjeEntity getOdeljenje() {
		return odeljenje;
	}
	public void setOdeljenje(OdeljenjeEntity odeljenje) {
		this.odeljenje = odeljenje;
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
	
	
	
	
	
}
