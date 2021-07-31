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
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.security.Views;

@Entity
@Table(name = "razred")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class RazredEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@JsonView(Views.Private.class)
	@NotNull
	@Min(value=1, message="Razred mora biti od 1 do 8!")
	@Max(value=8, message="Razred mora biti od 1 do 8!")
	@Column(nullable=false)
	private Integer oznakaRazreda;
	
	@JsonView(Views.Private.class)
	@OneToMany(mappedBy="razred", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
//	@JsonManagedReference(value="odeljenjeRazred")
	@JsonIgnore
	private List<OdeljenjeEntity> odeljenja; 
	
	@JsonView(Views.Private.class)
	@OneToMany(mappedBy="razred", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JsonManagedReference(value="razredPredmet")
	@JsonIgnore
	private List<PredmetEntity> predmeti;
	
	@Version
	private Integer version;

	
	public RazredEntity() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Integer getId() {
		return id;
	}

	
	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getOznakaRazreda() {
		return oznakaRazreda;
	}


	public void setOznakaRazreda(Integer oznakaRazreda) {
		this.oznakaRazreda = oznakaRazreda;
	}


	public List<OdeljenjeEntity> getOdeljenja() {
		return odeljenja;
	}


	public void setOdeljenja(List<OdeljenjeEntity> odeljenja) {
		this.odeljenja = odeljenja;
	}


	public List<PredmetEntity> getPredmeti() {
		return predmeti;
	}


	public void setPredmeti(List<PredmetEntity> predmeti) {
		this.predmeti = predmeti;
	}


	public Integer getVersion() {
		return version;
	}


	public void setVersion(Integer version) {
		this.version = version;
	}


	
	
}
