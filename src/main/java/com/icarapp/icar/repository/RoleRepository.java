package com.icarapp.icar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.icarapp.icar.model.Role;

import java.util.Optional;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String role);


    boolean existsByName(String role);
}
