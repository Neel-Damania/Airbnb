package com.neel.projects.airBnbApp.service;

import java.util.List;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.neel.projects.airBnbApp.dto.BookingDto;
import com.neel.projects.airBnbApp.dto.BookingRequest;
import com.neel.projects.airBnbApp.dto.GuestDto;

@Service
public interface BookingService {

    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDto);

    String initiatePayment(Long bookingId);

    void capturePayment(JSONObject jsonPayload);
    
}
