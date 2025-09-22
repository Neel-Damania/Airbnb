package com.neel.projects.airBnbApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.neel.projects.airBnbApp.entity.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
