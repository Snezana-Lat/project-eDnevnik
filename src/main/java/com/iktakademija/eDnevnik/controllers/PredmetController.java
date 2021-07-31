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
import com.iktakademija.eDnevnik.entities.PredmetEntity;
import com.iktakademija.eDnevnik.entities.RazredEntity;
import com.iktakademija.eDnevnik.entities.dto.PredmetDTO;
import com.iktakademija.eDnevnik.repositories.OdeljenjeRepository;
import com.iktakademija.eDnevnik.repositories.PredmetRepository;
import com.iktakademija.eDnevnik.repositories.RazredRepository;
import com.iktakademija.eDnevnik.security.Views;


@RestController
@RequestMapping("api/v1//predmet")

public class PredmetController {


	@Autowired
	RazredRepository razredRepository;
	
	@Autowired
	PredmetRepository predmetRepository;
	
	@Autowired
	OdeljenjeRepository odeljenjeRepository;
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}

	
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllPredmeti() {
		return new ResponseEntity<List<PredmetEntity>>((List<PredmetEntity>) predmetRepository.findAll(), HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPredmetById(@PathVariable Integer id) {
		if (!predmetRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predmet  ne postoji."), HttpStatus.NOT_FOUND);
		return new ResponseEntity<PredmetEntity>(predmetRepository.findById(id).get(), HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/razred/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getPredmetByRazredId(@PathVariable Integer id) {
		if (!razredRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Razred  ne postoji."), HttpStatus.NOT_FOUND);
		return new ResponseEntity<List<PredmetEntity>>((List<PredmetEntity>)razredRepository.findById(id).get().getPredmeti(), HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}",method = RequestMethod.PUT)
	public ResponseEntity<?> changePredmet( @Valid @PathVariable Integer id, @RequestBody PredmetDTO predmet, BindingResult result){
		
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		if (!predmetRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predmet  ne postoji."), HttpStatus.NOT_FOUND);
		PredmetEntity p=predmetRepository.findById(id).get();
		if(predmet.getNaziv()!=null)
		p.setNaziv(predmet.getNaziv());
		if(predmet.getNedeljniFondCasova()!=null)
		p.setNedeljniFondCasova(predmet.getNedeljniFondCasova());
		predmetRepository.save(p);
		logger.info("Admin je promenio predmet id"+p.getNaziv()+".");
		return new ResponseEntity<PredmetEntity>(p, HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createNewPredmet(@Valid @RequestBody PredmetEntity predmet, BindingResult result){
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if(predmetRepository.existsByNazivAndRazredId(predmet.getNaziv(), predmet.getRazred().getId())) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Vec postoji predmet sa tim nazivom u tom odeljenju."), HttpStatus.FORBIDDEN);
		}
		PredmetEntity p=new PredmetEntity();
		p.setNaziv(predmet.getNaziv());
		p.setNedeljniFondCasova(predmet.getNedeljniFondCasova());
		
		RazredEntity r=razredRepository.findById(predmet.getRazred().getId()).get();
		p.setRazred(r);
		r.getPredmeti().add(p);
		razredRepository.save(r);
		predmetRepository.save(p);
		logger.info("Admin je napravio predmet id"+p.getId()+".");
		return new ResponseEntity<PredmetEntity>(p, HttpStatus.OK);
	}
	
	
	
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deletePredmet(@PathVariable Integer id){
		
		if (!predmetRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predmet ne postoji!"), HttpStatus.NOT_FOUND);
		PredmetEntity p = predmetRepository.findById(id).get();
		PredmetDTO predmet=new PredmetDTO();
		predmet.setNaziv(p.getNaziv());
		predmet.setNedeljniFondCasova(p.getNedeljniFondCasova());
		predmetRepository.delete(p);
		logger.info("Admin je obrisao predmet"+p.getNaziv()+".");
		return new ResponseEntity<PredmetDTO>(predmet, HttpStatus.OK);
	}
	
}
