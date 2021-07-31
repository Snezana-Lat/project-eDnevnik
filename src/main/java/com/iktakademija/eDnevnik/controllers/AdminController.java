package com.iktakademija.eDnevnik.controllers;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktakademija.eDnevnik.controllers.utils.RESTError;
import com.iktakademija.eDnevnik.entities.NastavnikEntity;
import com.iktakademija.eDnevnik.entities.OdeljenjeEntity;
import com.iktakademija.eDnevnik.entities.PredajeEntity;
import com.iktakademija.eDnevnik.entities.PredmetEntity;
import com.iktakademija.eDnevnik.entities.UcenikEntity;
import com.iktakademija.eDnevnik.entities.UserEntity;
import com.iktakademija.eDnevnik.entities.dto.PredajeDTO;
import com.iktakademija.eDnevnik.entities.dto.PredajeDTOUpdate;
import com.iktakademija.eDnevnik.repositories.NastavnikPredmetRepository;
import com.iktakademija.eDnevnik.repositories.NastavnikRepository;
import com.iktakademija.eDnevnik.repositories.OcenaRepository;
import com.iktakademija.eDnevnik.repositories.OdeljenjeRepository;
import com.iktakademija.eDnevnik.repositories.PredajeRepository;
import com.iktakademija.eDnevnik.repositories.PredmetRepository;
import com.iktakademija.eDnevnik.repositories.RazredRepository;
import com.iktakademija.eDnevnik.repositories.RoditeljRepository;
import com.iktakademija.eDnevnik.repositories.RoleRepository;
import com.iktakademija.eDnevnik.repositories.UcenikRepository;
import com.iktakademija.eDnevnik.repositories.UserRepository;
import com.iktakademija.eDnevnik.security.Views;
import com.iktakademija.eDnevnik.services.FileHandler;
import com.iktakademija.eDnevnik.services.UserDAO;

@RestController
@RequestMapping("api/v1/users")

public class AdminController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	PredmetRepository predmetRepository;

	@Autowired
	UcenikRepository ucenikRepository;

	@Autowired
	NastavnikRepository nastavnikRepository;

	@Autowired
	RoditeljRepository roditeljRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	OdeljenjeRepository odeljenjeRepository;

	@Autowired
	OcenaRepository ocenaRepository;

	@Autowired
	RazredRepository razredRepository;

	@Autowired
	UserDAO userService;

	@Autowired
	PredajeRepository predajeRepository;

	@Autowired
	NastavnikPredmetRepository nastavnikPredmetRepository;

	@Autowired
	FileHandler fileHandler;

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllUsers() {
		logger.info("Admin je video sve korisnike.");
		return new ResponseEntity<List<UserEntity>>((List<UserEntity>) userRepository.findAll(), HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserById(@PathVariable Integer id) {
		if(!userRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Korisnik nije pronadjen."), HttpStatus.NOT_FOUND);
		}
		logger.info("Admin je video "+ userRepository.findById(id).get()+ " korisnika.");
		return new ResponseEntity<UserEntity>(userRepository.findById(id).get(), HttpStatus.OK);
	}

	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/dodajUcenika/{uId}/uOdeljenje/{oId}", method = RequestMethod.PUT)
	public ResponseEntity<?> dodajUcenikaUOdeljenje(@PathVariable Integer oId, @PathVariable Integer uId) {

		if (!odeljenjeRepository.existsById(oId))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Odeljenje  nije pronadjeno."), HttpStatus.NOT_FOUND);
		OdeljenjeEntity o = odeljenjeRepository.findById(oId).get();

		if (!ucenikRepository.existsById(uId))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ucenik nije pronadjen."), HttpStatus.NOT_FOUND);
		UcenikEntity u = ucenikRepository.findById(uId).get();
		o.getUcenici().add(u);
		u.setOdeljenje(o);
		ucenikRepository.save(u);
		odeljenjeRepository.save(o);
		logger.info("Admin je dodao "+ u.getName()+" "+u.getLastname()+" u odeljenje "+o.getRazred().getOznakaRazreda()+"-"+o.getOznakaOdeljenja()+".");
		return new ResponseEntity<UcenikEntity>(u, HttpStatus.OK);
	}

	/*
	 * @JsonView(Views.Admin.class)
	 * 
	 * @Secured("ROLE_ADMIN")
	 * 
	 * @RequestMapping(path = "/predaje", method = RequestMethod.POST) public
	 * ResponseEntity<?> predaje(@RequestParam Integer oId, @RequestParam Integer
	 * nId, @RequestParam Integer pId) { OdeljenjeEntity o =
	 * odeljenjeRepository.findById(oId).get(); NastavnikEntity n =
	 * nastavnikRepository.findById(nId).get(); PredmetEntity p =
	 * predmetRepository.findById(pId).get(); PredajeEntity predaje = new
	 * PredajeEntity();
	 * 
	 * if (!o.getRazred().getPredmeti().contains(p)) { return new
	 * ResponseEntity<RESTError>(new RESTError(2, "Predmet ne pripada razredu."),
	 * HttpStatus.FORBIDDEN);
	 * 
	 * } else if (nastavnikPredmetRepository.findByNastavnikIdAndPredmetId(nId, pId)
	 * == null) { return new ResponseEntity<RESTError>(new RESTError(2,
	 * "Nastavnik nije ovlascen da predaje zadati predmet."), HttpStatus.FORBIDDEN);
	 * } else { predaje.setNastavnik(n); predaje.setOdeljenje(o);
	 * predaje.setPredmet(p); predajeRepository.save(predaje); return new
	 * ResponseEntity<PredajeEntity>(predaje, HttpStatus.OK); }
	 * 
	 * }
	 */

	//PREDAJE
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/predaje", method = RequestMethod.POST)
	public ResponseEntity<?> napraviPredaje(@Valid @RequestBody PredajeDTO predaje, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (!odeljenjeRepository.existsById(predaje.getOdeljenjeId()))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Odeljenje nije pronadjeno."), HttpStatus.NOT_FOUND);
		OdeljenjeEntity o = odeljenjeRepository.findById(predaje.getOdeljenjeId()).get();

		if (!nastavnikRepository.existsById(predaje.getNastavnikId()))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik nije pronadjen."), HttpStatus.NOT_FOUND);
		NastavnikEntity n = nastavnikRepository.findById(predaje.getNastavnikId()).get();

		if (!predmetRepository.existsById(predaje.getPredmetId()))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predmet nije pronadjen."), HttpStatus.NOT_FOUND);
		
		if(predajeRepository.existsByPredmetIdAndNastavnikIdAndOdeljenjeId(predaje.getPredmetId(), predaje.getNastavnikId(), predaje.getOdeljenjeId())) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Nastavnik vec predaje taj predmet u tom odeljneju."), HttpStatus.FORBIDDEN);
		}
		
		
		PredmetEntity p = predmetRepository.findById(predaje.getPredmetId()).get();

		PredajeEntity predajeEntity = new PredajeEntity();

		if (!o.getRazred().getPredmeti().contains(p)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Predmet ne pripada razredu."), HttpStatus.FORBIDDEN);

		} else if (nastavnikPredmetRepository.findByNastavnikIdAndPredmetId(predaje.getNastavnikId(),
				predaje.getPredmetId()) == null) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Nastavnik nije ovlascen da predaje zadati predmet."),
					HttpStatus.FORBIDDEN);
		} else {
			predajeEntity.setNastavnik(n);
			predajeEntity.setOdeljenje(o);
			predajeEntity.setPredmet(p);
			predajeRepository.save(predajeEntity);
			logger.info("Napravljen je novi PredajeEntity.");
			return new ResponseEntity<PredajeEntity>(predajeEntity, HttpStatus.OK);
		}

	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/izmeniPredaje/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> predaje(@PathVariable Integer id, @Valid @RequestBody PredajeDTOUpdate predaje,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (!predajeRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Ne postoji predaje sa tim id."),
					HttpStatus.FORBIDDEN);
		}

		PredajeEntity pred = predajeRepository.findById(id).get();

		if (predaje.getOdeljenjeId() != null) {
			if (!odeljenjeRepository.existsById(predaje.getOdeljenjeId())) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Odeljenje nije pronadjeno."),
						HttpStatus.NOT_FOUND);
			}
			OdeljenjeEntity o = odeljenjeRepository.findById(predaje.getOdeljenjeId()).get();
			pred.setOdeljenje(o);
		}

		if (predaje.getNastavnikId() != null) {
			if (!nastavnikRepository.existsById(predaje.getNastavnikId())) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik nije pronadjen."),
						HttpStatus.NOT_FOUND);
			}
			NastavnikEntity n = nastavnikRepository.findById(predaje.getNastavnikId()).get();
			pred.setNastavnik(n);
		}

		if (predaje.getPredmetId() != null) {
			if (!predmetRepository.existsById(predaje.getPredmetId())) {
				return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predemet nije pronadjen."),
						HttpStatus.NOT_FOUND);
			}
			PredmetEntity p = predmetRepository.findById(predaje.getPredmetId()).get();
			pred.setPredmet(p);
		}
		if (nastavnikPredmetRepository.findByNastavnikIdAndPredmetId(pred.getNastavnik().getId(),
				pred.getPredmet().getId()) == null) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Nastavnik nije ovlascen da predaje zadati predmet."),
					HttpStatus.FORBIDDEN);
		} else if (!pred.getOdeljenje().getRazred().getPredmeti().contains(pred.getPredmet())) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Predmet ne pripada razredu."), HttpStatus.FORBIDDEN);
		}

		predajeRepository.save(pred);
		logger.info("Admin je izmenio PredajeEntity(id "+pred.getId()+"." );
		return new ResponseEntity<PredajeEntity>(pred, HttpStatus.OK);

	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/predaje/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> obrisiPredaje(@PathVariable Integer id){
		
		if(!predajeRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nije pronadjen taj  predaje entitet."), HttpStatus.NOT_FOUND);
		}
		PredajeEntity p=predajeRepository.findById(id).get();
		predajeRepository.delete(p);
		return new ResponseEntity<PredajeEntity>(p, HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/downloadLog")

	public ResponseEntity<?> downloadLog(HttpServletResponse response) throws IOException {
		fileHandler.downloadLog(response);
		return new ResponseEntity<>(HttpStatus.OK);

	}
}