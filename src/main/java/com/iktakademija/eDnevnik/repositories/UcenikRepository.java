package com.iktakademija.eDnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.eDnevnik.entities.UcenikEntity;

public interface UcenikRepository extends CrudRepository<UcenikEntity, Integer> {
	UcenikEntity findByUsername(String username);
	Boolean existsByJmbg(String jmbg);
}
