package com.iktakademija.eDnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.eDnevnik.entities.PredmetEntity;

public interface PredmetRepository extends CrudRepository<PredmetEntity, Integer> {

	Boolean existsByNazivAndRazredId(String naziv, Integer razredId);
}
