package com.iktakademija.eDnevnik.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.iktakademija.eDnevnik.controllers.utils.RESTError;
import com.iktakademija.eDnevnik.entities.UserEntity;
import com.iktakademija.eDnevnik.entities.dto.PasswordDTO;
import com.iktakademija.eDnevnik.entities.dto.UserTokenDTO;
import com.iktakademija.eDnevnik.repositories.UserRepository;

import com.iktakademija.eDnevnik.utils.Encryption;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;





@RestController
public class UserController {

	@Value("${spring.security.secret-key}")
	private String secretKey;
	
	@Value("${spring.security.token-duration}")
	private Integer tokenDuration;

	@Autowired
	UserRepository userRepository;
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
	}
	
	
	
	
	
	@RequestMapping(path = "/user/changePassword", method = RequestMethod.PUT)
	public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordDTO pass, BindingResult result){
		if (result.hasErrors()) {
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserEntity user=userRepository.findByUsername(auth.getPrincipal().toString());
	if(	Encryption.validatePassword(pass.getOldPass(), user.getPassword())) {
			user.setPassword(Encryption.getPassEncoded(pass.getNewPass()));
	      userRepository.save(user);
	      return new ResponseEntity<>("Uspesno promenjen password", HttpStatus.OK);
	}else
		return new ResponseEntity<RESTError>(new RESTError(2,"Nije dobro unet stari password"), HttpStatus.BAD_REQUEST);
	}
	
	
	
	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestParam String username, @RequestParam String password) {
		UserEntity user = userRepository.findByUsername(username);
		if (user != null && Encryption.validatePassword(password, user.getPassword())) {
			String token = getJWTToken(user);
			UserTokenDTO userDTO = new UserTokenDTO(user.getUsername(), token);
			return new ResponseEntity<>(userDTO, HttpStatus.OK);
		}
		return new ResponseEntity<>("Password/username don't match", HttpStatus.UNAUTHORIZED);
	}

	private String getJWTToken(UserEntity user) {

		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(user.getRole().getName());
		String token = Jwts.builder().setId("softtekJWT").setSubject(user.getUsername())
				.claim("authorities",
						grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + this.tokenDuration))
				.signWith(SignatureAlgorithm.HS512, this.secretKey.getBytes()).compact();
		return "Bearer " + token;

	}
	
	

	
	
	

}
