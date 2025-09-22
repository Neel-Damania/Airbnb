package com.neel.projects.airBnbApp.strategy;

import java.math.BigDecimal;
import java.time.LocalDate;


import com.neel.projects.airBnbApp.entity.Inventory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UrgencyPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        LocalDate today = LocalDate.now();
        if(!inventory.getDate().isBefore(today) && inventory.getDate().isBefore(today.plusDays(7))) {
            price = price.multiply(BigDecimal.valueOf(1.15));
        }
        return price;
    }

}
