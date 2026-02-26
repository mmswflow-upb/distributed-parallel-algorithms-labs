package com.tutorial.tutorial.repository;

import java.util.Optional;

import com.tutorial.tutorial.models.ERole;
import com.tutorial.tutorial.models.Role;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}