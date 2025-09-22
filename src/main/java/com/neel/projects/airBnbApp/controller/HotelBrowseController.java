package com.neel.projects.airBnbApp.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neel.projects.airBnbApp.dto.HotelInfoDto;
import com.neel.projects.airBnbApp.dto.HotelPriceDto;
import com.neel.projects.airBnbApp.dto.HotelSearchRequest;
import com.neel.projects.airBnbApp.service.HotelService;
import com.neel.projects.airBnbApp.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;
    @PostMapping("/search")
    public ResponseEntity<Page<HotelPriceDto> > browseHotels(@RequestBody HotelSearchRequest request) {
        Page<HotelPriceDto> page= inventoryService.searchHotels(request);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelById(@PathVariable Long hotelId){
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }
}
