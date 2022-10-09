package com.example.demo.repository;
import com.example.demo.model.Place;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface PlaceRepository extends MongoRepository<Place, String> {
    @Query("{businessName:'?0'}")
    Place findPlaceByName(String name);

    @Query(value="{category:'?0'}", fields="{'category' :1 ,'date':1,'location':1,'description':1,'cover':1,'businessName':1,'city':1,'address':1,'county':1,'capacity':1,'currentCapacity':1,'importantPlace':1,'events':1,photos:1,'phoneNumber':1}")
    List<Place> findAll(String category);

    public long count();
}
