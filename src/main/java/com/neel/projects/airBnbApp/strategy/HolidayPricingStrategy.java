package com.neel.projects.airBnbApp.strategy;

import java.math.BigDecimal;


import com.neel.projects.airBnbApp.entity.Inventory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HolidayPricingStrategy implements PricingStrategy {


    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        Boolean isHoliday = true; //call an api with todays date
        if(isHoliday) {
            price = price.multiply(BigDecimal.valueOf(1.25));
        }
        return price;
    }


}
