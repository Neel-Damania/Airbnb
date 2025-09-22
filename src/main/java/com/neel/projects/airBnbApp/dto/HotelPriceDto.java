package com.neel.projects.airBnbApp.dto;

import com.neel.projects.airBnbApp.entity.Hotel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelPriceDto {
    private Hotel hotel;  
    private Double price; 
}

