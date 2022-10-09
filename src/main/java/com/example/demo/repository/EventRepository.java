package com.example.demo.repository;

import com.example.demo.model.Event;
import com.example.demo.model.Place;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {
    @Query(value="{placeId:'?0'}", fields="{'placeId' :1,'name' :1 ,'description':1,'date':1,'category':1,'cover':1,'strDate':1}")
    List<Event> findAll(String placeId);

    public long count();
}
