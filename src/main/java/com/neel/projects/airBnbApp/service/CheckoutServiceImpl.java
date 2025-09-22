package com.neel.projects.airBnbApp.service;

import java.math.BigDecimal;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.neel.projects.airBnbApp.entity.Booking;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutServiceImpl implements CheckoutService{

    @Value("${razorpay.api.key}")
    private String apiKey;

    @Value("${razorpay.api.secret}")
    private String apiSecret;
    
    public String getCheckoutSession(Booking booking)  {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(apiKey, apiSecret);
            JSONObject orderRequest = new JSONObject();
            
            orderRequest.put("amount", booking.getAmount().multiply(BigDecimal.valueOf(100)).doubleValue());
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", booking.getId().toString());
            
            Order order = razorpayClient.orders.create(orderRequest);
            return order.toString(); 
        }catch (RazorpayException e) {
            e.printStackTrace();
            log.error("Razorpay Exception");
            throw new RuntimeException("Razorpay Exception");
        } 
    }

}
