package com.iktakademija.eDnevnik.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;

@Entity
@Table(name = "predaje")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class PredajeEntity {
	
	@JsonView(Views.Public.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@JsonView(Views.Private.class)
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="nastavnik")
	//@JsonBackReference(value="nastavnikPredaje")
	private NastavnikEntity nastavnik;
	
	@JsonView(Views.Private.class)
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="odeljenje")
	//@JsonBackReference(value="odeljenjePredaje")
	private OdeljenjeEntity odeljenje;
	
	@JsonView(Views.Private.class)
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="predmet")
	//@JsonBackReference(value="predmetPredaje")
	private PredmetEntity predmet;
	
	@Version
	private Integer version;
	
	public PredajeEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public NastavnikEntity getNastavnik() {
		return nastavnik;
	}
	public void setNastavnik(NastavnikEntity nastavnik) {
		this.nastavnik = nastavnik;
	}
	public OdeljenjeEntity getOdeljenje() {
		return odeljenje;
	}
	public void setOdeljenje(OdeljenjeEntity odeljenje) {
		this.odeljenje = odeljenje;
	}
	public PredmetEntity getPredmet() {
		return predmet;
	}
	public void setPredmet(PredmetEntity predmet) {
		this.predmet = predmet;
	}
	
	
	

}
