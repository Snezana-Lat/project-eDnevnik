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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;

@Entity
@Table(name = "predmet")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PredmetEntity {

	@JsonView(Views.Public.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@JsonView(Views.Private.class)
	@NotBlank(message="Morate uneti naziv predmeta.")
	@Column(nullable=false)
	private String naziv;

	@JsonView(Views.Private.class)
	@NotNull
	@Column(nullable=false)
	private Integer nedeljniFondCasova;

	@JsonView(Views.Private.class)
	@OneToMany(mappedBy = "predmet", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	//@JsonManagedReference(value = "predmetPredaje")
	@JsonIgnore
	private List<PredajeEntity> predaje;

	@OneToMany(mappedBy = "predmet", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	//@JsonManagedReference(value = "ocenaPredmet")
	@JsonIgnore
	private List<OcenaEntity> ocene;

	@JsonView(Views.Private.class)
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "razred")
	@JsonBackReference(value = "razredPredmet")
	private RazredEntity razred;
	
	@JsonView(Views.Private.class)
	@OneToMany(mappedBy="nastavnik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonIgnore
//	@JsonManagedReference(value="predemtNastavnik")
	List<NastavnikPredmet> nastavnici;

	@Version
	private Integer version;

	public PredmetEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		id = id;
	}

	public String getNaziv() {
		return naziv;
	}

	public void setNaziv(String naziv) {
		this.naziv = naziv;
	}

	public Integer getNedeljniFondCasova() {
		return nedeljniFondCasova;
	}

	public void setNedeljniFondCasova(Integer nedeljniFondCasova) {
		this.nedeljniFondCasova = nedeljniFondCasova;
	}

	public List<PredajeEntity> getPredaje() {
		return predaje;
	}

	public void setPredaje(List<PredajeEntity> predaje) {
		this.predaje = predaje;
	}

	public List<OcenaEntity> getOcene() {
		return ocene;
	}

	public void setOcene(List<OcenaEntity> ocene) {
		this.ocene = ocene;
	}

	public RazredEntity getRazred() {
		return razred;
	}

	public void setRazred(RazredEntity razred) {
		this.razred = razred;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
