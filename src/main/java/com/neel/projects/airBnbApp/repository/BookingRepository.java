package com.neel.projects.airBnbApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.neel.projects.airBnbApp.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking,Long>{

    Booking findByPaymentSessionId(String orderId);

}
