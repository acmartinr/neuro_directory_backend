package com.example.demo.controller;


import com.example.demo.model.Booking;
import com.example.demo.model.Event;
import com.example.demo.model.Place;
import com.example.demo.repository.BookingRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.PlaceRepository;
import com.example.demo.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@Controller
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    PlaceRepository placeRepository;


    @PostMapping("/")
    public @ResponseBody
    Event addEventAdmin(@RequestPart("newEvent") Event newEvent, @RequestPart("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        newEvent.setPhotos(fileName);
        Event e = eventRepository.save(newEvent);
        String uploadDir = "user-photos/" + e.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        Update update = new Update();
        update.push("events", newEvent);
        BasicQuery query = new BasicQuery("{id:'"+newEvent.getPlaceId()+"'}");
        mongoTemplate.updateFirst(query, update, Place.class, "Place");
        return e;
    }

    @PutMapping("/")
    public @ResponseBody
    Event updateEventAdmin(@RequestPart("newEvent") Event newEvent, @RequestPart("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        String uniqueID = newEvent.getId();
        Optional<Event> entity=eventRepository.findById(uniqueID);
        if(fileName.equals("no_image.jpg")){
            newEvent.setPhotos(entity.get().getPhotos());
        }else{
            newEvent.setPhotos(fileName);
            String uploadDir = "user-photos/" + newEvent.getId();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        Event e = eventRepository.save(newEvent);
        Place p = placeRepository.findById(e.getPlaceId()).get();
        List<Event> newEvents = new ArrayList<>();
        for(Event ev: p.getEvents()){
            if(ev.getId().equals(newEvent.getId())){
                newEvents.add(e);
            }else{
                newEvents.add(ev);
            }

        }
        p.setEvents(newEvents);
        placeRepository.save(p);
        /*
        Update update = new Update();
        update.push("events", newEvent);
        BasicQuery query = new BasicQuery("{id:'"+newEvent.getPlaceId()+"'}");
        mongoTemplate.updateFirst(query, update, Place.class, "Place");

         */
        return e;
    }


    @DeleteMapping("/deleteevent/{placeId}/{eventId}")
    public @ResponseBody String deleteEvent(@PathVariable String placeId,@PathVariable String eventId) {
        try{
            List<Event> oldEvents= placeRepository.findById(placeId).get().getEvents();
            List<Event> newEvents = new ArrayList<>();
            for(Event e : oldEvents){
                if(!e.getId().equals(eventId)){
                    System.out.println("adding");
                    newEvents.add(e);
                }else{
                    System.out.println("no adding");
                }
            }
            Update update = new Update();
            update.set("events", newEvents);
            BasicQuery query = new BasicQuery("{id:'"+placeId+"'}");
            mongoTemplate.updateFirst(query, update, Place.class, "Place");
            eventRepository.deleteById(eventId);
            return "OK";
        }catch (Exception e){
            return "BAD";
        }
    }
}
