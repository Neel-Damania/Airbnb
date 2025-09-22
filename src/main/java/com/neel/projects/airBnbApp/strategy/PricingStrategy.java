package com.neel.projects.airBnbApp.strategy;

import java.math.BigDecimal;


import com.neel.projects.airBnbApp.entity.Inventory;

public interface PricingStrategy {
    public BigDecimal calculatePrice(Inventory inventory);
}
