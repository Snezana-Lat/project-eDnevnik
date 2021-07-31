package com.iktakademija.eDnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.eDnevnik.entities.NastavnikEntity;

public interface NastavnikRepository extends CrudRepository<NastavnikEntity, Integer> {
	
	NastavnikEntity findByUsername(String username);
	Boolean existsByJmbg(String jmbg);
}
