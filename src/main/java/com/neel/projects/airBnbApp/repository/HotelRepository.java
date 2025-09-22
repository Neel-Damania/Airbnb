package com.neel.projects.airBnbApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neel.projects.airBnbApp.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

}
