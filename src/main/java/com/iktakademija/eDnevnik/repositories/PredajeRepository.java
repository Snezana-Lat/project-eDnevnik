package com.iktakademija.eDnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.eDnevnik.entities.PredajeEntity;

public interface PredajeRepository extends CrudRepository<PredajeEntity, Integer> {

	
	PredajeEntity findByNastavnikIdAndOdeljenjeIdAndPredmetId(Integer nastavnikId, Integer odeljenjeId, Integer predmetId);
	List<PredajeEntity> findByNastavnikId(Integer nastavnikId);
	
	List<PredajeEntity> findByNastavnikIdAndOdeljenjeId(Integer nastavnikId, Integer odeljenjeId);
	
	Boolean existsByPredmetIdAndNastavnikIdAndOdeljenjeId(Integer pId, Integer nId, Integer oId);
}
