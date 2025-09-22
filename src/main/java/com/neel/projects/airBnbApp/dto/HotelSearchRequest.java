package com.neel.projects.airBnbApp.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class HotelSearchRequest {
    private String city;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer roomsCount;
    private Integer page = 0;
    private Integer size = 10;
}
