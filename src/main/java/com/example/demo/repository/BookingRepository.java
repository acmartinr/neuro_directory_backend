package com.example.demo.repository;

import com.example.demo.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, String> {
    @Query(value="{email:'?0'}",fields="{'email' :1,'fullname' :1 ,'ci':1,'personCount':1,'eventId':1}")
    List<Booking> findAll(String placeId);

    public long count();
}
