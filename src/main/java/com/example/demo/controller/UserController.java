package com.example.demo.controller;


import com.example.demo.model.Config;
import com.example.demo.repository.ConfigRepository;
import com.example.demo.repository.PlaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    ConfigRepository configRepository;


    @GetMapping("/config")
    public @ResponseBody
    Config config() {
        return configRepository.findAll().get(0);
    }
}
