package com.iktakademija.eDnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.eDnevnik.entities.NastavnikPredmet;

public interface NastavnikPredmetRepository extends CrudRepository<NastavnikPredmet, Integer> {
	
	NastavnikPredmet findByNastavnikIdAndPredmetId(Integer nastavnikId, Integer predmetId);

	Boolean existsByNastavnikIdAndPredmetId(Integer nId, Integer pId);
}
