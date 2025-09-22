package com.neel.projects.airBnbApp.strategy;

import java.math.BigDecimal;


import com.neel.projects.airBnbApp.entity.Inventory;

public class BasePricingStrategy implements PricingStrategy {

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }

}
