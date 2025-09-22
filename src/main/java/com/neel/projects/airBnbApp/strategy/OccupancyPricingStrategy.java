package com.neel.projects.airBnbApp.strategy;

import java.math.BigDecimal;

import com.neel.projects.airBnbApp.entity.Inventory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OccupancyPricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);
        double occupancyRate = (double) inventory.getBookedCount() / inventory.getTotalCount();
        if(occupancyRate > 0.8) {
            price = price.multiply(new BigDecimal(1.2));
        }
        return price;

    }

}
