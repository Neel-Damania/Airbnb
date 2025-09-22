package com.neel.projects.airBnbApp.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RoomDto {
    private Long id;
    private String type;
    private BigDecimal basePrice;
    private String[] photos;
    private String[] amenities;
    private Integer totalCount;
    private Integer capacity;

}
