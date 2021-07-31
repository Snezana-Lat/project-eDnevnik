package com.iktakademija.eDnevnik.entities;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.enums.EOcena;
import com.iktakademija.eDnevnik.security.Views;


@Entity
@Table(name = "ocena")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class OcenaEntity {

	@JsonView(Views.Public.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@JsonView(Views.Private.class)
	@NotNull(message = "Morate uneti ocenu od 1 do 5.")
	@Min(value=1, message="Ocena mora biti od 1 do 5!")
	@Max(value=5, message="Ocena mora biti od 1 do 5!")
	@Column(nullable=false)
	private Integer ocena;
	
	@Version
	private Integer version;
	
	
	@JsonView(Views.Private.class)
	//@NotBlank(message = "Unesite oznaku ocene.")
	private EOcena  oznaka;
	
	@JsonView(Views.Private.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern="dd-MM-yyyy")
	@Column(nullable=false)
	private LocalDate datumOcenjivanja;
	
	@JsonView(Views.Private.class)
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="ucenik")
	//@JsonBackReference(value="ucenikOcena")
	private UcenikEntity ucenik;
	
	@JsonView(Views.Private.class)
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="predmet")
	//@JsonBackReference(value="ocenaPredmet")
	private PredmetEntity predmet;

	@JsonView(Views.Private.class)
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.LAZY)
	@JoinColumn(name="nastavnik")
	//@JsonBackReference(value="ocenaNastavnik")
	private NastavnikEntity nastavnik;
	
	public OcenaEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOcena() {
		return ocena;
	}

	public void setOcena(Integer ocena) {
		this.ocena = ocena;
	}

	public EOcena getOznaka() {
		return oznaka;
	}

	public void setOznaka(EOcena oznaka) {
		this.oznaka = oznaka;
	}

	public LocalDate getDatumOcenjivanja() {
		return datumOcenjivanja;
	}

	public void setDatumOcenjivanja(LocalDate datumOcenjivanja) {
		this.datumOcenjivanja = datumOcenjivanja;
	}

	public UcenikEntity getUcenik() {
		return ucenik;
	}

	public void setUcenik(UcenikEntity ucenik) {
		this.ucenik = ucenik;
	}

	public PredmetEntity getPredmet() {
		return predmet;
	}

	public void setPredmet(PredmetEntity predmet) {
		this.predmet = predmet;
	}

	public NastavnikEntity getNastavnik() {
		return nastavnik;
	}

	public void setNastavnik(NastavnikEntity nastavnik) {
		this.nastavnik = nastavnik;
	}
	
	
	
	
	 
}
