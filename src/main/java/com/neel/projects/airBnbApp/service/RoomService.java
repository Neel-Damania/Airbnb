package com.neel.projects.airBnbApp.service;

import java.util.List;

import com.neel.projects.airBnbApp.dto.RoomDto;

public interface RoomService {
    RoomDto createNewRoom(Long hotelId,RoomDto roomDto);
    List<RoomDto> getAllRoomsInHotel(Long hotelId);
    RoomDto getRoomById(Long id);
    void deleteRoomById(Long roomId);
}
