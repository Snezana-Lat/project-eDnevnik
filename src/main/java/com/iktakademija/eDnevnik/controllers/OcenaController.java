package com.iktakademija.eDnevnik.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
import com.iktakademija.eDnevnik.entities.OcenaEntity;
import com.iktakademija.eDnevnik.entities.UcenikEntity;
import com.iktakademija.eDnevnik.entities.dto.OcenaDTO;
import com.iktakademija.eDnevnik.repositories.NastavnikRepository;
import com.iktakademija.eDnevnik.repositories.OcenaRepository;
import com.iktakademija.eDnevnik.repositories.PredmetRepository;
import com.iktakademija.eDnevnik.repositories.UcenikRepository;
import com.iktakademija.eDnevnik.security.Views;

@RestController
@RequestMapping("api/v1/ocena")
public class OcenaController {

	@Autowired
	OcenaRepository ocenaRepository;
	
	@Autowired
	PredmetRepository predmetRepository;
	
	@Autowired
	UcenikRepository ucenikRepository;
	
	@Autowired
	NastavnikRepository nastavnikRepository;
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}
	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllOcene() {
		logger.info("Admin je video sve ocene.");
		return new ResponseEntity<List<OcenaEntity>>((List<OcenaEntity>) ocenaRepository.findAll(), HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOceneById(@PathVariable Integer id) {
		if(!ocenaRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ocena nije pronadjena."), HttpStatus.NOT_FOUND);
		}
		logger.info("Admin je video ocenu.");
		return new ResponseEntity<OcenaEntity>(ocenaRepository.findById(id).get(), HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/ucenik/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOceneByUcenikId(@PathVariable Integer id) {
		if(!ucenikRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ucenik nije pronadjen."), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<OcenaEntity>>((List<OcenaEntity>) ucenikRepository.findById(id).get().getOcene(), HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/nastavnik/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOceneByNastavnikId(@PathVariable Integer id) {
		if(!nastavnikRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik nije pronadjen."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<OcenaEntity>>((List<OcenaEntity>) nastavnikRepository.findById(id).get().getOcene(), HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/predmet/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOceneByPredmetId(@PathVariable Integer id) {
		if(!predmetRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ne postoji taj predmet"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<OcenaEntity>>((List<OcenaEntity>) predmetRepository.findById(id).get().getOcene(), HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createNewOcena( @Valid @RequestBody OcenaDTO ocena, BindingResult result){
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		OcenaEntity o=new OcenaEntity();
		o.setDatumOcenjivanja(LocalDate.now());
		o.setOcena(ocena.getOcena());
		o.setOznaka(ocena.getOznaka());
		ocenaRepository.save(o);
		logger.info("Admin je napravio ocenu id-"+o.getId());
		return new ResponseEntity<OcenaEntity>(o, HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteOcena(@PathVariable Integer id){
		
		if (!ocenaRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ocena nije nadjena."), HttpStatus.NOT_FOUND);
		OcenaEntity ocena=ocenaRepository.findById(id).get();
		ocenaRepository.delete(ocena);
		logger.info("Admin je obrisao ocenu id-"+ocena.getId());
		return new ResponseEntity<OcenaEntity>(ocena, HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}",method = RequestMethod.PUT)
	public ResponseEntity<?> changeOcena( @Valid @PathVariable Integer id, @RequestBody OcenaDTO ocena, BindingResult result){
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		
		if (!ocenaRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ocena nije nadjena."), HttpStatus.NOT_FOUND);
		OcenaEntity o = ocenaRepository.findById(id).get();
		if(ocena.getOznaka()!=null)
		o.setOznaka(ocena.getOznaka());
		if(ocena.getOcena()!=null)
		o.setOcena(ocena.getOcena());
		o.setDatumOcenjivanja(LocalDate.now());
		
		
		ocenaRepository.save(o);
		logger.info("Admin je izmenio ocenu id-"+o.getId());
		return new ResponseEntity<OcenaEntity>(o, HttpStatus.OK);
	}
	
}
