package com.neel.projects.airBnbApp.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neel.projects.airBnbApp.entity.Hotel;
import com.neel.projects.airBnbApp.entity.HotelMinPrice;
import com.neel.projects.airBnbApp.entity.Inventory;
import com.neel.projects.airBnbApp.repository.HotelMinPriceRepository;
import com.neel.projects.airBnbApp.repository.HotelRepository;
import com.neel.projects.airBnbApp.repository.InventoryRepository;
import com.neel.projects.airBnbApp.strategy.PricingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PriceUpdateService {

    private final HotelRepository hotelRepository;
    private final InventoryRepository inventoryRepository;
    private final HotelMinPriceRepository hotelMinPriceRepository;
    private final PricingService pricingService;

    @Scheduled(cron = "0 0 * * * *")
    public void updatePrices() {
        log.info("Cron Job triggered : Updating prices for hotels");
        int page = 0;
        int batchSize = 100;
        while(true) {
           Page<Hotel> hotelPage = hotelRepository.findAll(PageRequest.of(page, batchSize));
           if(hotelPage.isEmpty()) {
               break;
           }

           hotelPage.getContent().forEach(hotel->updateHotelPrices(hotel));
           

           page++;
        }
    }
    private void updateHotelPrices(Hotel hotel) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusYears(1);

        List<Inventory> inventoryList = inventoryRepository.findByHotelAndDateBetween(hotel, startDate, endDate);
        updateInventoryPrices(inventoryList);

        updateHotelMinPrices(hotel, inventoryList,startDate,endDate);
    }

    private void updateInventoryPrices(List<Inventory> inventoryList) {
        inventoryList.forEach(inventory->{
            BigDecimal price = pricingService.calculateDynamicPrice(inventory);
            inventory.setPrice(price);
        });

        inventoryRepository.saveAll(inventoryList);
    }

    private void updateHotelMinPrices(Hotel hotel, List<Inventory> inventoryList, LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, BigDecimal> dailyMinPrice = inventoryList.stream()
                .collect(Collectors.groupingBy(
                    Inventory::getDate,
                    Collectors.mapping(Inventory::getPrice, Collectors.minBy(Comparator.naturalOrder()))
                )).entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e->e.getValue().orElse(BigDecimal.ZERO)));

                List<HotelMinPrice> hotelPrices = new ArrayList<>();
                
                dailyMinPrice.forEach((date,price)->{
                    HotelMinPrice hotelMinPrice = hotelMinPriceRepository.findByHotelAndDate(hotel,date).orElse(new HotelMinPrice(hotel,date));
                    hotelMinPrice.setPrice(price);
                    hotelPrices.add(hotelMinPrice);
                });

                hotelMinPriceRepository.saveAll(hotelPrices); 

    }

}
