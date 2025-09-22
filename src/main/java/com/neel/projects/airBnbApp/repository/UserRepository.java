package com.neel.projects.airBnbApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neel.projects.airBnbApp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

}
