package com.neel.projects.airBnbApp.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.neel.projects.airBnbApp.dto.BookingDto;
import com.neel.projects.airBnbApp.dto.BookingRequest;
import com.neel.projects.airBnbApp.dto.GuestDto;
import com.neel.projects.airBnbApp.entity.Booking;
import com.neel.projects.airBnbApp.entity.Guest;
import com.neel.projects.airBnbApp.entity.Hotel;
import com.neel.projects.airBnbApp.entity.Inventory;
import com.neel.projects.airBnbApp.entity.Room;
import com.neel.projects.airBnbApp.entity.User;
import com.neel.projects.airBnbApp.entity.enums.BookingStatus;
import com.neel.projects.airBnbApp.exception.ResourceNotFoundException;
import com.neel.projects.airBnbApp.exception.UnAuthorisedException;
import com.neel.projects.airBnbApp.repository.BookingRepository;
import com.neel.projects.airBnbApp.repository.GuestRepository;
import com.neel.projects.airBnbApp.repository.HotelRepository;
import com.neel.projects.airBnbApp.repository.InventoryRepository;
import com.neel.projects.airBnbApp.repository.RoomRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final GuestRepository guestRepository;
    private final CheckoutService checkoutService;

    @Override
    @Transactional
    public BookingDto initialiseBooking(BookingRequest bookingRequest) {
        log.info("Initialising booking for hotel : {}, room: {}, date {}-{}",bookingRequest.getHotelId(),bookingRequest.getRoomId(),bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate());
        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId()).orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id: "+ bookingRequest.getHotelId()));
        Room room = roomRepository.findById(bookingRequest.getRoomId()).orElseThrow(()-> new ResourceNotFoundException("Hotel not found with id: "+ bookingRequest.getRoomId()));
    
        List<Inventory> inventoryList= inventoryRepository.findAndLockAvailableInventory(room.getId(),bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate(),bookingRequest.getRoomsCount());
        Long daysCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(),bookingRequest.getCheckOutDate())+1;
        if(inventoryList.size()!=daysCount){
            throw new IllegalStateException("Room is not available anymore.");
        }
        
        for(Inventory inventory:inventoryList){
            inventory.setReservedCount(inventory.getReservedCount()+bookingRequest.getRoomsCount());
        }
        inventoryRepository.saveAll(inventoryList);
        User user = getCurrentUser();

        //TODO: Calculate dynamic price
        Booking booking = Booking.builder()
                            .bookingStatus(BookingStatus.RESERVED)
                            .hotel(hotel)
                            .room(room)
                            .checkInDate(bookingRequest.getCheckInDate())
                            .checkOutDate(bookingRequest.getCheckOutDate())
                            .user(user)
                            .roomsCount(bookingRequest.getRoomsCount())
                            .amount(BigDecimal.TEN)
                            .build();


        booking = bookingRepository.save(booking);
        return modelMapper.map(booking,BookingDto.class);

    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("Adding guests to booking with id: {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()-> new ResourceNotFoundException("Booking not found with id: "+ bookingId));
        User user = getCurrentUser();

        if(!user.equals(booking.getUser())){
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }

        if(user.equals(booking.getUser()))

        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking is expired.");
        }
        if(booking.getBookingStatus()!=BookingStatus.RESERVED){
            throw new IllegalStateException("Booking is not in reserved state. Cannot add guests.");
        }
        for(GuestDto guestDto:guestDtoList){
            Guest guest = modelMapper.map(guestDto,Guest.class);
            guest.setUser(user);
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);
        }
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);

        return modelMapper.map(booking,BookingDto.class);
    }

    public Boolean hasBookingExpired(Booking booking){
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public String initiatePayment(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(()-> new ResourceNotFoundException("Booking not found with id: "+ bookingId));
        User user = getCurrentUser();

        if(!user.equals(booking.getUser())){
            throw new UnAuthorisedException("Booking does not belong to this user with id: "+user.getId());
        }
        
        if(hasBookingExpired(booking)){
            throw new IllegalStateException("Booking is expired.");
        }
        String sessionUrl = checkoutService.getCheckoutSession(booking);
        booking.setBookingStatus(BookingStatus.PAYMENTS_PENDING);
        booking.setPaymentSessionId(sessionUrl);
        bookingRepository.save(booking);
        return sessionUrl;
    }

    @Override
    @Transactional
    public void capturePayment(JSONObject jsonPayload) {
        
        if (jsonPayload.has("payload") && jsonPayload.getJSONObject("payload").has("payment")) {
            JSONObject paymentEntity = jsonPayload.getJSONObject("payload").getJSONObject("payment").getJSONObject("entity");

            String paymentId = paymentEntity.getString("id");
            String orderId = paymentEntity.getString("order_id");
            String status = paymentEntity.getString("status");
            double amount = paymentEntity.getDouble("amount") / 100.0; // Convert from paise to INR

            System.out.println("✅ Order ID: " + orderId);
            System.out.println("✅ Payment ID: " + paymentId);
            System.out.println("✅ Status: " + status);
            System.out.println("✅ Amount: " + amount + " INR");

            // Process based on payment status
            if ("captured".equals(status)) {
                // ✅ Update order status in database here
                Booking booking = bookingRepository.findByPaymentSessionId(orderId);
                booking.setBookingStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
                inventoryRepository.findAndLockReservedInventory(booking.getRoom().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());
                inventoryRepository.confirmBooking(booking.getRoom().getId(), booking.getCheckInDate(), booking.getCheckOutDate(), booking.getRoomsCount());
                log.info("🎉 Payment Captured Successfully for Booking ID: " + booking.getId());
            } else if ("failed".equals(status)) {
                System.out.println("❌ Payment Failed for Order ID: " + orderId);
                // ❌ Handle payment failure scenario
            }
        }
    }

}
