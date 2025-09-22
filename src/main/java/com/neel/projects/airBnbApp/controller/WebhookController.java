package com.neel.projects.airBnbApp.controller;

import org.springframework.web.bind.annotation.RestController;

import com.neel.projects.airBnbApp.service.BookingService;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/webhook")
public class WebhookController {

    @Value("${razorpay.api.secret}")
    private String apiSecret;

    private final BookingService bookingService;
    @GetMapping("/payment")
    public ResponseEntity<String> webhooks(HttpServletRequest request) {
        return ResponseEntity.ok().build();
        // try {
        //     String payload = new BufferedReader(request.getReader()).lines().collect(Collectors.joining("\n"));
        //     JSONObject jsonPayload = new JSONObject(payload);
        //     System.out.println("Received Webhook: " + jsonPayload.toString(2));
        //     String razorpaySignature = request.getHeader("X-Razorpay-Signature");
            
        //     if(Utils.verifyWebhookSignature(payload, razorpaySignature, apiSecret)) {
        //         return ResponseEntity.badRequest().build();
        //     }
        //     bookingService.capturePayment(jsonPayload);
        //     return ResponseEntity.ok().build();
        // } catch (Exception e) {
        //     throw new RuntimeException(e);
        // }
    }
    @PostMapping("/payment")
    public ResponseEntity<String> webhook(HttpServletRequest request) {
        return ResponseEntity.ok().build();
        // try {
        //     String payload = new BufferedReader(request.getReader()).lines().collect(Collectors.joining("\n"));
        //     JSONObject jsonPayload = new JSONObject(payload);
        //     System.out.println("Received Webhook: " + jsonPayload.toString(2));
        //     String razorpaySignature = request.getHeader("X-Razorpay-Signature");
            
        //     if(Utils.verifyWebhookSignature(payload, razorpaySignature, apiSecret)) {
        //         return ResponseEntity.badRequest().build();
        //     }
        //     bookingService.capturePayment(jsonPayload);
        //     return ResponseEntity.ok().build();
        // } catch (Exception e) {
        //     throw new RuntimeException(e);
        // }
    }

    @GetMapping("/test")
    public String getResponse() throws InterruptedException {
        log.info("Inside getResposnse");
        Thread.sleep(1000);
        log.info("After sleep");
        return "Success";
    }

     
}
