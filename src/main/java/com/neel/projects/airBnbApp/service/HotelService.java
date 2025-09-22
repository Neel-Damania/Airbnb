package com.neel.projects.airBnbApp.service;

import com.neel.projects.airBnbApp.dto.HotelDto;
import com.neel.projects.airBnbApp.dto.HotelInfoDto;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotel);
    HotelDto getHotelById(Long id);
    HotelDto updateHotelById(Long hotelId, HotelDto hotelDto);
    Boolean deleteHotelById(Long hotelId);
    void activateHotel(Long hotelId);
    HotelInfoDto getHotelInfoById(Long hotelId);
}
