package com.example.demo.controller;


import com.example.demo.model.Booking;
import com.example.demo.model.Place;
import com.example.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/api/booking")
public class BookingController {

    @Autowired
    BookingRepository bookingRepository;

    @PostMapping("/")
    public @ResponseBody String booking(@RequestBody Booking booking) throws IOException {
        Booking b = bookingRepository.save(booking);
        return "200";
    }
}
