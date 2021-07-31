package com.iktakademija.eDnevnik.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
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
import com.iktakademija.eDnevnik.entities.NastavnikEntity;
import com.iktakademija.eDnevnik.entities.NastavnikPredmet;
import com.iktakademija.eDnevnik.entities.OcenaEntity;
import com.iktakademija.eDnevnik.entities.OdeljenjeEntity;
import com.iktakademija.eDnevnik.entities.PredajeEntity;
import com.iktakademija.eDnevnik.entities.PredmetEntity;
import com.iktakademija.eDnevnik.entities.UcenikEntity;
import com.iktakademija.eDnevnik.entities.dto.NastavnikDTO;
import com.iktakademija.eDnevnik.entities.dto.OcenaDTO;
import com.iktakademija.eDnevnik.entities.dto.UserDTO;
import com.iktakademija.eDnevnik.repositories.NastavnikPredmetRepository;
import com.iktakademija.eDnevnik.repositories.NastavnikRepository;
import com.iktakademija.eDnevnik.repositories.OcenaRepository;
import com.iktakademija.eDnevnik.repositories.OdeljenjeRepository;
import com.iktakademija.eDnevnik.repositories.PredajeRepository;
import com.iktakademija.eDnevnik.repositories.PredmetRepository;
import com.iktakademija.eDnevnik.repositories.RoleRepository;
import com.iktakademija.eDnevnik.repositories.UcenikRepository;
import com.iktakademija.eDnevnik.repositories.UserRepository;
import com.iktakademija.eDnevnik.security.Views;
import com.iktakademija.eDnevnik.services.EmailServiceImpl;
import com.iktakademija.eDnevnik.services.OcenaDAOImpl;
import com.iktakademija.eDnevnik.utils.Encryption;

@RestController
@RequestMapping("api/v1/users/nastavnik")
public class NastavnikController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	NastavnikRepository nastavnikRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	OcenaRepository ocenaRepository;

	@Autowired
	PredmetRepository predmetRepository;

	@Autowired
	NastavnikPredmetRepository nastavnikPredmetRepository;

	@Autowired
	PredajeRepository predajeRepository;

	@Autowired
	UcenikRepository ucenikRepository;

	@Autowired
	EmailServiceImpl emailService;

	@Autowired
	OdeljenjeRepository odeljenjeRepository;
	
	@Autowired
	OcenaDAOImpl ocenaDao;

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}

	private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAllNastavnik() {
		return new ResponseEntity<List<NastavnikEntity>>((List<NastavnikEntity>) nastavnikRepository.findAll(),
				HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getNastavnikById(@PathVariable Integer id) {
		if(!nastavnikRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik nije pronadjen."), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<NastavnikEntity>(nastavnikRepository.findById(id).get(), HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> createNewNastavnik(@Valid @RequestBody NastavnikDTO nastavnik, BindingResult result) {
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		if(nastavnikRepository.existsByJmbg(nastavnik.getJmbg())) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Vec postoji nastavnik sa tim jmbg."),HttpStatus.FORBIDDEN );
		}
		NastavnikEntity n = new NastavnikEntity(nastavnik.getName(), nastavnik.getLastname());
		n.setRole(roleRepository.findByName("ROLE_NASTAVNIK"));
		n.setJmbg(nastavnik.getJmbg());
		n.setUsername(n.getName().toLowerCase().substring(0, 1) + n.getLastname().toLowerCase()
				+ n.getJmbg().substring(0, 7));
		n.setBrLicence(nastavnik.getBrLicence());
		n.setPassword(Encryption.getPassEncoded(
				n.getName().toLowerCase().substring(0, 3) + n.getLastname().toLowerCase().substring(0, 3)));

		nastavnikRepository.save(n);
		logger.info("Admin je napravio novog nastavnika: " + n.getName() + " " + n.getLastname() + ".");
		return new ResponseEntity<NastavnikEntity>(n, HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteNastavnik(@PathVariable Integer id) {

		if (!nastavnikRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik not found!"), HttpStatus.NOT_FOUND);
		NastavnikEntity n = nastavnikRepository.findById(id).get();
		UserDTO u = new UserDTO();
		u.setName(n.getName());
		u.setLastname(n.getLastname());
		nastavnikRepository.delete(n);
		logger.info("Admin je obrisao nastavnika: " + n.getName() + " " + n.getLastname() + ".");
		return new ResponseEntity<UserDTO>(u, HttpStatus.OK);
	}

	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> promeniNastavnik(@Valid @PathVariable Integer id, @RequestBody NastavnikDTO nastavnik,
			BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (!nastavnikRepository.existsById(id))
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik nije nadjen"), HttpStatus.NOT_FOUND);
		NastavnikEntity n = nastavnikRepository.findById(id).get();
		if (nastavnik.getLastname() != null)
			n.setLastname(nastavnik.getLastname());
		if (nastavnik.getName() != null)
			n.setName(nastavnik.getName());
		if (nastavnik.getBrLicence() != null)
			n.setBrLicence(nastavnik.getBrLicence());
		if (nastavnik.getJmbg() != null)
			n.setJmbg(nastavnik.getJmbg());
		nastavnikRepository.save(n);
		logger.info("Admin je promenio nastavnika: " + n.getName() + " " + n.getLastname() + ".");
		return new ResponseEntity<NastavnikEntity>(n, HttpStatus.OK);
	}

	//DODAJ PREDMET NASTAVNIKU
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/{nId}/predmet/{pId}", method = RequestMethod.POST)
	public ResponseEntity<?> dodajPredmetNastavniku(@PathVariable Integer nId, @PathVariable Integer pId) {
		NastavnikPredmet np = new NastavnikPredmet();
		if (!nastavnikRepository.existsById(nId)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik nije nadjen."), HttpStatus.NOT_FOUND);
		}
		if (!predmetRepository.existsById(pId)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predmet nije nadjen."), HttpStatus.NOT_FOUND);
		}
		if(nastavnikPredmetRepository.existsByNastavnikIdAndPredmetId(nId, pId)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.FORBIDDEN.value(), "Nastavnik je vec povezan sa predmetom"),HttpStatus.FORBIDDEN);
		}
		NastavnikEntity n = nastavnikRepository.findById(nId).get();
		np.setNastavnik(n);
		np.setPredmet(predmetRepository.findById(pId).get());
		nastavnikPredmetRepository.save(np);
		logger.info("Admin je dodao predmet " + np.getPredmet().getNaziv() + " nastavniku  " + n.getName() + " "
				+ n.getLastname() + ".");
		return new ResponseEntity<NastavnikPredmet>(np, HttpStatus.OK);

	}
	
	@JsonView(Views.Admin.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(path = "/obrisiNastavnikPredmet/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> obrisiNastavnikPredmet(@PathVariable Integer id){
		if(!nastavnikPredmetRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nije pronadjena veza nastavnika i predmeta."),HttpStatus.NOT_FOUND);
		}
		NastavnikPredmet np=nastavnikPredmetRepository.findById(id).get();
		nastavnikPredmetRepository.delete(np);
		return new ResponseEntity<NastavnikPredmet>(np, HttpStatus.OK);
	}

	@JsonView(Views.Private.class)
	@Secured("ROLE_NASTAVNIK")
	@RequestMapping(value = "/ocene", method = RequestMethod.GET)
	public ResponseEntity<?> vidiOcene() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		NastavnikEntity nastavnik = nastavnikRepository
				.findById(userRepository.findByUsername(auth.getPrincipal().toString()).getId()).get();
		logger.info("Nastavnik" + nastavnik.getName() + " " + nastavnik.getLastname() + "je video ocene.");
		return new ResponseEntity<List<OcenaEntity>>(
				(List<OcenaEntity>) ocenaRepository.findByNastavnikId(nastavnik.getId()), HttpStatus.OK);
	}

	@JsonView(Views.Private.class)
	@Secured("ROLE_NASTAVNIK")
	@RequestMapping(value = "/dajOcenu/ucenik/{uId}/predmet/{pId}", method = RequestMethod.POST)
	public ResponseEntity<?> dajOcenu(@Valid @PathVariable Integer uId, @PathVariable Integer pId,
			@RequestBody OcenaEntity o, BindingResult result) {

		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		if (!ucenikRepository.existsById(uId)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ucenik nije nadjen."), HttpStatus.NOT_FOUND);
		}
		if (!predmetRepository.existsById(pId)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Predmet nije nadjen."), HttpStatus.NOT_FOUND);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		NastavnikEntity nastavnik = nastavnikRepository
				.findById(userRepository.findByUsername(auth.getPrincipal().toString()).getId()).get();
		UcenikEntity ucenik = ucenikRepository.findById(uId).get();
		PredmetEntity predmet = predmetRepository.findById(pId).get();
		if (predajeRepository.findByNastavnikIdAndOdeljenjeIdAndPredmetId(nastavnik.getId(),
				ucenik.getOdeljenje().getId(), predmet.getId()) == null) {
			return new ResponseEntity<RESTError>(
					(new RESTError(HttpStatus.FORBIDDEN.value(), "Nastavnik ne predaje zadati predmet zadatom uceniku")), HttpStatus.FORBIDDEN);
		} else {
			OcenaEntity ocena = new OcenaEntity();
			ocena.setUcenik(ucenik);
			ucenik.getOcene().add(ocena);

			ocena.setPredmet(predmet);
			predmet.getOcene().add(ocena);
			ocena.setNastavnik(nastavnik);
			nastavnik.getOcene().add(ocena);
			ocena.setOznaka(o.getOznaka());
			ocena.setOcena(o.getOcena());
			ocena.setDatumOcenjivanja(LocalDate.now());
			ocenaRepository.save(ocena);
			ucenikRepository.save(ucenik);
			nastavnikRepository.save(nastavnik);
			predmetRepository.save(predmet);

			try {
				emailService.posaljiMailRoditelju(ocena);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			logger.info("Nastavnik" + nastavnik.getName() + " " + nastavnik.getLastname() + "je ocenio ucenika"
					+ ucenik.getName() + " " + ucenik.getLastname() + ".");
			return new ResponseEntity<OcenaEntity>(ocena, HttpStatus.OK);

		}

	}

	@JsonView(Views.Private.class)
	@Secured("ROLE_NASTAVNIK")
	@RequestMapping(value = "/sveOcene", method = RequestMethod.GET)
	public ResponseEntity<?> vidiOceneIzPredmetaKojiPredajeUOdeljenju() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		NastavnikEntity nastavnik = nastavnikRepository.findByUsername(auth.getPrincipal().toString());
		List<PredajeEntity> nastavnikPredaje = predajeRepository.findByNastavnikId(nastavnik.getId());
		List<OcenaEntity> ocene = new ArrayList<OcenaEntity>();
		for (PredajeEntity p : nastavnikPredaje) {
			OdeljenjeEntity odeljenje = odeljenjeRepository.findById(p.getOdeljenje().getId()).get();
			List<UcenikEntity> ucenici = odeljenje.getUcenici();
			for (UcenikEntity u : ucenici) {
				List<OcenaEntity> uo = u.getOcene();
				for (OcenaEntity o : uo) {
					if (o.getPredmet() == p.getPredmet()) {
						ocene.add(o);
					}
				}
			}

		}
		logger.info("Nastavnik" + nastavnik.getName() + " " + nastavnik.getLastname() + "je video ocene.");
		return new ResponseEntity<List<OcenaEntity>>(ocene, HttpStatus.OK);
	}

	@JsonView(Views.Private.class)
	@Secured("ROLE_NASTAVNIK")
	@RequestMapping(value = "/vidiOcenu/{oId}", method = RequestMethod.GET)
	public ResponseEntity<?> vidiOcenuPoId(@PathVariable Integer oId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		NastavnikEntity nastavnik = nastavnikRepository.findByUsername(auth.getPrincipal().toString());
		if (!ocenaRepository.existsById(oId)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ocena ne postoji."), HttpStatus.NOT_FOUND);
		}
		OcenaEntity o = ocenaRepository.findById(oId).get();
		if (o.getNastavnik().equals(nastavnik)) {
			return new ResponseEntity<OcenaEntity>(o, HttpStatus.OK);
		} else
			logger.info("Nastavnik" + nastavnik.getName() + " " + nastavnik.getLastname() + "je video ocene.");
		return new ResponseEntity<RESTError>(new RESTError(3, "Nastavnik nema ovlascenje da vidi ocenu."),
				HttpStatus.UNAUTHORIZED);
	}

	/*@JsonView(Views.Private.class)
	@Secured("ROLE_NASTAVNIK")
	@RequestMapping(value = "/sveOcenePoOdeljenju/{oId}", method = RequestMethod.GET)
	public ResponseEntity<?> vidiOcenePoOdeljenju(@PathVariable Integer oId) {
		if (!odeljenjeRepository.existsById(oId)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Odeljenje nije nadjeno"), HttpStatus.NOT_FOUND);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		NastavnikEntity nastavnik = nastavnikRepository.findByUsername(auth.getPrincipal().toString());
		OdeljenjeEntity odeljenje = odeljenjeRepository.findById(oId).get();
		List<PredajeEntity> nastavnikPredaje = predajeRepository.findByNastavnikIdAndOdeljenjeId(nastavnik.getId(),
				odeljenje.getId());

		if (nastavnikPredaje.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik ne predaje u tom odeljenju"),
					HttpStatus.NOT_FOUND);
		}
		List<OcenaEntity> ocene = new ArrayList<OcenaEntity>();
		for (PredajeEntity p : nastavnikPredaje) {
			List<UcenikEntity> ucenici = odeljenje.getUcenici();
			for (UcenikEntity u : ucenici) {
				List<OcenaEntity> uo = u.getOcene();
				for (OcenaEntity o : uo) {
					if (o.getPredmet() == p.getPredmet()) {
						ocene.add(o);
					}
				}
			}

		}
		logger.info("Nastavnik"+nastavnik.getName()+" "+nastavnik.getLastname()+"je video ocene.");
		return new ResponseEntity<List<OcenaEntity>>(ocene, HttpStatus.OK);

	}*/
	
	@JsonView(Views.Private.class)
	@Secured("ROLE_NASTAVNIK")
	@RequestMapping(value = "/sveOcenePoOdeljenju/{oId}", method = RequestMethod.GET)
	public ResponseEntity<?> vidiOcenePoOdeljenju(@PathVariable Integer oId) {
		if (!odeljenjeRepository.existsById(oId)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Odeljenje nije nadjeno"), HttpStatus.NOT_FOUND);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		NastavnikEntity nastavnik = nastavnikRepository.findByUsername(auth.getPrincipal().toString());
		OdeljenjeEntity odeljenje=odeljenjeRepository.findById(oId).get();
		
		List<PredajeEntity> nastavnikPredaje = predajeRepository.findByNastavnikIdAndOdeljenjeId(nastavnik.getId(),
				odeljenje.getId());

		if (nastavnikPredaje.size() == 0) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Nastavnik ne predaje u tom odeljenju"),
					HttpStatus.NOT_FOUND);
		}
		List<OcenaEntity> ocene = ocenaDao.findOcenaByNastavnikAndOdeljenje(nastavnik, odeljenje);
		logger.info("Nastavnik"+nastavnik.getName()+" "+nastavnik.getLastname()+"je video ocene.");
		return new ResponseEntity<List<OcenaEntity>>(ocene, HttpStatus.OK);

	}
		
		

	@JsonView(Views.Private.class)
	@Secured("ROLE_NASTAVNIK")
	@RequestMapping(value = "/promeniOcenu/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> promeniOcenu(@PathVariable Integer id, @RequestBody OcenaDTO ocena) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		NastavnikEntity nastavnik = nastavnikRepository.findByUsername(auth.getPrincipal().toString());
		if (!ocenaRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ocena nije nadjen."), HttpStatus.NOT_FOUND);
		}
		OcenaEntity o = ocenaRepository.findById(id).get();
		if (o.getNastavnik().equals(nastavnik)) {
			if (ocena.getOcena() != null)
				o.setOcena(ocena.getOcena());
			if (ocena.getOznaka() != null)
				o.setOznaka(ocena.getOznaka());
			ocenaRepository.save(o);
			logger.info("Nastavnik"+nastavnik.getName()+" "+nastavnik.getLastname()+"je izmenio ocenu "+o.getId()+".");
			return new ResponseEntity<OcenaEntity>(o, HttpStatus.OK);

		} else
			logger.info("Nastavnik"+nastavnik.getName()+" "+nastavnik.getLastname()+"je pokusao da izmeni ocenu");
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.UNAUTHORIZED.value(), "Nastavnik nema ovlascenje da menja zadatu ocenu."),
					HttpStatus.UNAUTHORIZED);

	}

	@JsonView(Views.Private.class)
	@Secured("ROLE_NASTAVNIK")
	@RequestMapping(value = "/obrisiOcenu/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> izbrisiOcenu(@PathVariable Integer id) {
		if (!ocenaRepository.existsById(id)) {
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.NOT_FOUND.value(), "Ocena nije nadjena."), HttpStatus.NOT_FOUND);
		}

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		NastavnikEntity nastavnik = nastavnikRepository.findByUsername(auth.getPrincipal().toString());
		OcenaEntity o = ocenaRepository.findById(id).get();
		if (o.getNastavnik().equals(nastavnik)) {
			ocenaRepository.delete(o);
			logger.info("Nastavnik"+nastavnik.getName()+" "+nastavnik.getLastname()+"je obrisao ocenu id-"+ o.getId());
			return new ResponseEntity<OcenaEntity>(o, HttpStatus.OK);
		} else
			logger.info("Nastavnik"+nastavnik.getName()+" "+nastavnik.getLastname()+"je pokusao da obrise ocenu");
			return new ResponseEntity<RESTError>(new RESTError(HttpStatus.UNAUTHORIZED.value(), "Nastavnik nema ovlascenje da obrise zadatu ocenu."),
					HttpStatus.UNAUTHORIZED);
	}
}
