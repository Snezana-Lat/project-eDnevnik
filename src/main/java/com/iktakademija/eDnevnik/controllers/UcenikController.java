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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.iktakademija.eDnevnik.entities.OdeljenjeEntity;
import com.iktakademija.eDnevnik.entities.RoditeljEntity;
import com.iktakademija.eDnevnik.entities.UcenikEntity;

import com.iktakademija.eDnevnik.entities.dto.UcenikDTO;
import com.iktakademija.eDnevnik.entities.dto.UserDTO;
import com.iktakademija.eDnevnik.repositories.OcenaRepository;
import com.iktakademija.eDnevnik.repositories.OdeljenjeRepository;
import com.iktakademija.eDnevnik.repositories.RoditeljRepository;
import com.iktakademija.eDnevnik.repositories.RoleRepository;
import com.iktakademija.eDnevnik.repositories.UcenikRepository;
import com.iktakademija.eDnevnik.repositories.UserRepository;
import com.iktakademija.eDnevnik.security.Views;
import com.iktakademija.eDnevnik.utils.Encryption;

@RestController
@RequestMapping("api/v1/users/ucenik")
public class UcenikController {

	@Autowired
	UcenikRepository ucenikRepository;

	@Autowired
	RoditeljRepository roditeljRepository;

	@Autowired
	OdeljenjeRepository odeljenjeRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	OcenaRepository ocenaRepository;

	@Autowired
	UserRepository userRepository;

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllUcenik() {
		return new ResponseEntity<List<UcenikEntity>>((List<UcenikEntity>) ucenikRepository.findAll(), HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getUcenikById(@PathVariable Integer id) {
		if (!ucenikRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ne postoji ucenik!"), HttpStatus.NOT_FOUND);
		return new ResponseEntity<UcenikEntity>(ucenikRepository.findById(id).get(), HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createNewUcenik(@Valid @RequestBody UcenikDTO ucenik,  BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if(ucenikRepository.existsByJmbg(ucenik.getJmbg())) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Vec postoji ucenik sa tim jmbg."), HttpStatus.FORBIDDEN);
		}
		UcenikEntity u = new UcenikEntity(ucenik.getName(), ucenik.getLastname());
		u.setJmbg(ucenik.getJmbg());
		u.setRole(roleRepository.findByName("ROLE_UCENIK"));
		u.setUsername(u.getName().toLowerCase().substring(0, 1) + u.getLastname().toLowerCase()
				+ u.getJmbg().substring(0, 7)+"U");
		// u.setPassword(Encryption.getPassEncoded(userService.makePassword(8)));
		u.setPassword(Encryption.getPassEncoded(
				u.getName().toLowerCase().substring(0, 3) + u.getLastname().toLowerCase().substring(0, 3)));
	
		ucenikRepository.save(u);
		logger.info("Admin je napravio novog ucenika id-"+u.getId());
		return new ResponseEntity<UcenikEntity>(u, HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{uId}/odeljenje/{oId}", method = RequestMethod.PUT)
	public ResponseEntity<?> dodajUcenikaUodeljenje(@PathVariable Integer uId, @PathVariable Integer oId){
		if (!ucenikRepository.existsById(uId))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ne postoji ucenik!"), HttpStatus.NOT_FOUND);
		if (!odeljenjeRepository.existsById(oId))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ne postoji odeljenje."), HttpStatus.NOT_FOUND);
		
		UcenikEntity u = ucenikRepository.findById(uId).get();
		OdeljenjeEntity o=odeljenjeRepository.findById(oId).get();
		u.setOdeljenje(o);
		o.getUcenici().add(u);
		ucenikRepository.save(u);
		odeljenjeRepository.save(o);
	    logger.info("Ucenik " +u.getName()+" "+u.getLastname()+" je dodat u odeljenje "+ o.getRazred().getOznakaRazreda()+"-"+o.getOznakaOdeljenja()+".");
		return new ResponseEntity<UcenikEntity>(u, HttpStatus.OK);
		
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{uId}/roditelj/{rId}", method = RequestMethod.PUT)
	public ResponseEntity<?> dodajRoditeljaUceniku(@PathVariable Integer uId, @PathVariable Integer rId){
		if (!ucenikRepository.existsById(uId))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ne postoji ucenik."), HttpStatus.NOT_FOUND);
		if (!roditeljRepository.existsById(rId))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ne postoji roditelj."), HttpStatus.NOT_FOUND);
		
		UcenikEntity u = ucenikRepository.findById(uId).get();
		RoditeljEntity r=roditeljRepository.findById(rId).get();
		u.setRoditelj(r);
		r.getDeca().add(u);
		ucenikRepository.save(u);
		roditeljRepository.save(r);
		logger.info("Admin je dodao roditelja " +r.getName()+" "+r.getLastname() + " uceniku "+u.getName()+" "+u.getLastname());
		return new ResponseEntity<UcenikEntity>(u, HttpStatus.OK);
	}
		
	
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteUcenik(@PathVariable Integer id) {
		
		if (!ucenikRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(1, "Ucenik ne postoji"), HttpStatus.NOT_FOUND);
		
		UcenikEntity u = ucenikRepository.findById(id).get();
		UserDTO ucenik = new UserDTO();
		ucenik.setName(u.getName());
		ucenik.setLastname(u.getLastname());
		ucenikRepository.delete(u);
		logger.info("Admin je obrisao ucenika " +u.getId());
		return new ResponseEntity<UserDTO>(ucenik, HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> changeUcenik(@Valid @PathVariable Integer id, @RequestBody UcenikDTO ucenik, BindingResult result) {
		
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		if (!ucenikRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(1, "Ne postoji ucenik!"), HttpStatus.NOT_FOUND);
		}
		UcenikEntity u = ucenikRepository.findById(id).get();
		if(ucenik.getLastname()!=null)
		u.setLastname(ucenik.getLastname());
		if(ucenik.getName()!=null)
		u.setName(ucenik.getName());
		if(ucenik.getJmbg()!=null)
		u.setJmbg(ucenik.getJmbg());
		
		ucenikRepository.save(u);
		logger.info("Admin je izmenio ucenika id-"+u.getId());
		return new ResponseEntity<UcenikEntity>(u, HttpStatus.OK);
	}

	@JsonView(Views.Private.class)
	@Secured("ROLE_UCENIK")
	@RequestMapping(value = "/ocene", method = RequestMethod.GET)
	public ResponseEntity<?> vidiOcene() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UcenikEntity ucenik = ucenikRepository.findByUsername(auth.getPrincipal().toString());
		logger.info(ucenik.getName()+" "+ucenik.getLastname()+" je video svoje ocene.");
		return new ResponseEntity<List<OcenaEntity>>((List<OcenaEntity>) ocenaRepository.findByUcenikId(ucenik.getId()),
				HttpStatus.OK);
	}
}