package com.iktakademija.eDnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.eDnevnik.entities.RoditeljEntity;

public interface RoditeljRepository extends CrudRepository<RoditeljEntity, Integer> {

	RoditeljEntity findByUsername(String username);
	Boolean existsByJmbg(String jmbg);
	
}
