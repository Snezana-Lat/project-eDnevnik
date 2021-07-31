package com.iktakademija.eDnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.eDnevnik.entities.OdeljenjeEntity;

public interface OdeljenjeRepository extends CrudRepository<OdeljenjeEntity, Integer> {
	
	Boolean existsByOznakaOdeljenjaAndRazredOznakaRazreda(Integer odeljenje, Integer razred);

}
