package com.iktakademija.eDnevnik.repositories;

import org.springframework.data.repository.CrudRepository;

import com.iktakademija.eDnevnik.entities.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {

	UserEntity findByUsername(String username);
}
