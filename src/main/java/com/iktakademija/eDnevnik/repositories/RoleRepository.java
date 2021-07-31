package com.iktakademija.eDnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.eDnevnik.entities.RoleEntity;

public interface RoleRepository extends CrudRepository<RoleEntity, Integer> {

	RoleEntity findByName(String name);
}
