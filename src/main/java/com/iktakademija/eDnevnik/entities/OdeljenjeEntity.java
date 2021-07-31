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
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;

@Entity
@Table(name = "odeljenje")

@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class OdeljenjeEntity {

	@JsonView(Views.Public.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@JsonView(Views.Private.class)
	@NotNull
	@Column(nullable=false)
	private Integer oznakaOdeljenja;

	@JsonView(Views.Private.class)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "razred")
	//@JsonBackReference(value = "odeljenjeRazred")
	private RazredEntity razred;

	@Version
	private Integer version;

	@JsonView(Views.Private.class)
	@OneToMany(mappedBy = "odeljenje", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonManagedReference(value = "ucenikOdeljenje")
	@JsonIgnore
	private List<UcenikEntity> ucenici;

	@JsonView(Views.Private.class)
	@OneToMany(mappedBy = "odeljenje", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	//@JsonManagedReference(value = "odeljenjePredaje")
	@JsonIgnore
	private List<PredajeEntity> predaje;

	public OdeljenjeEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOznakaOdeljenja() {
		return oznakaOdeljenja;
	}

	public void setOznakaOdeljenja(Integer oznakaOdeljenja) {
		this.oznakaOdeljenja = oznakaOdeljenja;
	}

	public List<UcenikEntity> getUcenici() {
		return ucenici;
	}

	public void setUcenici(List<UcenikEntity> ucenici) {
		this.ucenici = ucenici;
	}

	public List<PredajeEntity> getPredaje() {
		return predaje;
	}

	public void setPredaje(List<PredajeEntity> predaje) {
		this.predaje = predaje;
	}

	public RazredEntity getRazred() {
		return razred;
	}

	public void setRazred(RazredEntity razred) {
		this.razred = razred;
	}

}
