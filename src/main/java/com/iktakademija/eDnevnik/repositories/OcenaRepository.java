package com.iktakademija.eDnevnik.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.eDnevnik.entities.OcenaEntity;


public interface OcenaRepository extends CrudRepository<OcenaEntity, Integer> {

	List<OcenaEntity> findByUcenikId(Integer id);
	
	List<OcenaEntity> findByNastavnikId(Integer id);
}
