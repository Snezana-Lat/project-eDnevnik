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
import com.iktakademija.eDnevnik.entities.RoditeljEntity;
import com.iktakademija.eDnevnik.entities.UcenikEntity;
import com.iktakademija.eDnevnik.entities.dto.RoditeljDTO;
import com.iktakademija.eDnevnik.entities.dto.UserDTO;
import com.iktakademija.eDnevnik.repositories.OcenaRepository;
import com.iktakademija.eDnevnik.repositories.RoditeljRepository;
import com.iktakademija.eDnevnik.repositories.RoleRepository;
import com.iktakademija.eDnevnik.repositories.UserRepository;
import com.iktakademija.eDnevnik.security.Views;
import com.iktakademija.eDnevnik.utils.Encryption;

@RestController
@RequestMapping("api/v1/users/roditelj")
public class RoditeljController {
	@Autowired
	RoditeljRepository roditeljRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	OcenaRepository ocenaRepository;
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}
	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());	
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping( method = RequestMethod.GET)
	public ResponseEntity<?> getAllRoditelj() {
		return new ResponseEntity<List<RoditeljEntity>>((List<RoditeljEntity>) roditeljRepository.findAll(), HttpStatus.OK);
	}
	
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getRoditeljById(@PathVariable Integer id) {
		if (!roditeljRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ne postoji taj roditelj."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<RoditeljEntity>(roditeljRepository.findById(id).get(), HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createNewRoditelj(@Valid @RequestBody RoditeljDTO roditelj, BindingResult result){
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}		
		if(roditeljRepository.existsByJmbg(roditelj.getJmbg())) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Vec postoji roditelj sa tim jmbg."), HttpStatus.FORBIDDEN);
		}
		RoditeljEntity r=new RoditeljEntity(roditelj.getName(), roditelj.getLastname(), roditelj.getEmail());
		r.setJmbg(roditelj.getJmbg());
		r.setRole(roleRepository.findByName("ROLE_RODITELJ"));
		r.setUsername(r.getName().toLowerCase().substring(0, 1)+r.getLastname().toLowerCase()+r.getJmbg().substring(0, 7)+"R");
		//r.setPassword(Encryption.getPassEncoded(userService.makePassword(8)));
		r.setPassword(Encryption.getPassEncoded(r.getName().toLowerCase().substring(0,3)+r.getLastname().toLowerCase().substring(0,3)) );
		roditeljRepository.save(r);
		logger.info("Admin je napravio roditelja " +r.getId());
		return new ResponseEntity<RoditeljEntity>(r, HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteRoditelj(@PathVariable Integer id){
		
		if (!roditeljRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ne postoji taj roditelj."), HttpStatus.NOT_FOUND);
		}
		RoditeljEntity r = roditeljRepository.findById(id).get();
		UserDTO u=new UserDTO();
		u.setName(r.getName());
		u.setLastname(r.getLastname());
		
		roditeljRepository.delete(r);
		logger.info("Admin je obrisao roditelja " +r.getId());
		return new ResponseEntity<UserDTO>(u, HttpStatus.OK);
	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}",method = RequestMethod.PUT)
	public ResponseEntity<?> changeRoditelj(@Valid @PathVariable Integer id, @RequestBody RoditeljDTO roditelj, BindingResult result){
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		
		if (!roditeljRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ne postoji taj roditelj."), HttpStatus.NOT_FOUND);
		}
		RoditeljEntity r = roditeljRepository.findById(id).get();
		if(roditelj.getEmail()!=null)
		r.setEmail(roditelj.getEmail());
		if(roditelj.getLastname()!=null)
		r.setLastname(roditelj.getLastname());
		if(roditelj.getName()!=null)
		r.setName(roditelj.getName());
		if(roditelj.getJmbg()!=null)
			r.setJmbg(roditelj.getName());
		roditeljRepository.save(r);
		logger.info("Admin je promenio roditelja " +r.getId());
		return new ResponseEntity<RoditeljEntity>(r, HttpStatus.OK);
	}
	
	@JsonView(Views.Private.class)
	@Secured("ROLE_RODITELJ")
	@RequestMapping(value = "/ocene/dete/{i}", method = RequestMethod.GET)
	public ResponseEntity<?> vidiOcene(@PathVariable Integer i) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	//	UserEntity roditelj = userRepository.findByUsername(auth.getPrincipal().toString());
		//RoditeljEntity r=roditeljRepository.findById(roditelj.getId()).get();
		
		RoditeljEntity r=roditeljRepository.findByUsername(auth.getPrincipal().toString());
		logger.info("Roditelj "+ r.getName()+" "+r.getLastname()+" je video ocene svoje dece.");
		return new ResponseEntity<List<OcenaEntity>>((List<OcenaEntity>) ocenaRepository.findByUcenikId(r.getDeca().get(i).getId()),
				HttpStatus.OK);
	}
	
	@JsonView(Views.Private.class)
	@Secured("ROLE_RODITELJ")
	@RequestMapping(value = "/deca", method = RequestMethod.GET)
	public ResponseEntity<?> vidiDecu() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      
		RoditeljEntity r=roditeljRepository.findByUsername(auth.getPrincipal().toString());
		logger.info("Roditelj "+ r.getName()+" "+r.getLastname()+" je video ocene svoje dece.");
		return new ResponseEntity<List<UcenikEntity>>((List<UcenikEntity>)r.getDeca(), HttpStatus.OK);
	}
}
