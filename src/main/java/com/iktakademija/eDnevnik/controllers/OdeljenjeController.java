package com.iktakademija.eDnevnik.controllers;

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
import com.iktakademija.eDnevnik.entities.OdeljenjeEntity;
import com.iktakademija.eDnevnik.entities.RazredEntity;
import com.iktakademija.eDnevnik.entities.dto.OdeljenjeDTO;
import com.iktakademija.eDnevnik.repositories.OdeljenjeRepository;
import com.iktakademija.eDnevnik.repositories.RazredRepository;
import com.iktakademija.eDnevnik.security.Views;

@RestController
@RequestMapping("api/v1/odeljenje")
public class OdeljenjeController {

	@Autowired
	OdeljenjeRepository odeljenjeRepository;

	@Autowired
	RazredRepository razredRepository;

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllOdeljenje() {
		logger.info("Admin je video odeljenja.");
		return new ResponseEntity<List<OdeljenjeEntity>>((List<OdeljenjeEntity>) odeljenjeRepository.findAll(),
				HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOdeljnjeById(@PathVariable Integer id) {
		return new ResponseEntity<OdeljenjeEntity>(odeljenjeRepository.findById(id).get(), HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/razred/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getOdeljenjaByRazredId(@PathVariable Integer id) {
		return new ResponseEntity<List<OdeljenjeEntity>>(
				(List<OdeljenjeEntity>) razredRepository.findById(id).get().getOdeljenja(), HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createNewOdeljenje(@Valid @RequestBody OdeljenjeDTO odeljenje, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if(odeljenjeRepository.existsByOznakaOdeljenjaAndRazredOznakaRazreda(odeljenje.getOznakaOdeljenja(), odeljenje.getRazred())) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Vec postoji takvo odeljenje"), HttpStatus.FORBIDDEN);
		}
		OdeljenjeEntity o = new OdeljenjeEntity();
		o.setOznakaOdeljenja(odeljenje.getOznakaOdeljenja());
		RazredEntity r = razredRepository.findById(odeljenje.getRazred()).get();
		o.setRazred(r);
		r.getOdeljenja().add(o);
		razredRepository.save(r);
		odeljenjeRepository.save(o);
		logger.info("Admin je napravio odeljenje"+o.getRazred().getOznakaRazreda()+"-"+o.getOznakaOdeljenja());
		return new ResponseEntity<OdeljenjeEntity>(o, HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteOdeljenje(@PathVariable Integer id) {

		if (!odeljenjeRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Odeljenje nije nadjeno!"), HttpStatus.NOT_FOUND);
		}
		OdeljenjeEntity o = odeljenjeRepository.findById(id).get();
		OdeljenjeDTO odeljenje = new OdeljenjeDTO();
		odeljenje.setOznakaOdeljenja(o.getOznakaOdeljenja());
		odeljenje.setRazred(o.getRazred().getOznakaRazreda());
		o.getRazred().getOdeljenja().remove(o);
		razredRepository.save(o.getRazred());
		odeljenjeRepository.delete(o);
		logger.info("Admin je obrisao odeljenje"+o.getRazred().getOznakaRazreda()+"-"+o.getOznakaOdeljenja());
		return new ResponseEntity<OdeljenjeDTO>(odeljenje, HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> changeOdeljenje(@Valid @PathVariable Integer id, @RequestBody OdeljenjeDTO odeljenje,
			BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		if (!odeljenjeRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Odeljenje  not found!"), HttpStatus.NOT_FOUND);
		OdeljenjeEntity o = odeljenjeRepository.findById(id).get();
		if (odeljenje.getOznakaOdeljenja() != null)
			o.setOznakaOdeljenja(odeljenje.getOznakaOdeljenja());
		if (odeljenje.getRazred() != null)
			o.setRazred(razredRepository.findById(odeljenje.getRazred()).get());
		odeljenjeRepository.save(o);
		logger.info("Admin je izmenio odeljenje"+o.getRazred().getOznakaRazreda()+"-"+o.getOznakaOdeljenja());
		return new ResponseEntity<OdeljenjeEntity>(o, HttpStatus.OK);
	}
}
