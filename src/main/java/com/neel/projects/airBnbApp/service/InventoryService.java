package com.neel.projects.airBnbApp.service;
import org.springframework.data.domain.Page;

import com.neel.projects.airBnbApp.dto.HotelPriceDto;
import com.neel.projects.airBnbApp.dto.HotelSearchRequest;
import com.neel.projects.airBnbApp.entity.Room;

public interface InventoryService {
    void initializeRoomForAYear(Room room);

    void deleteAllInventories(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchRequest request);

}
