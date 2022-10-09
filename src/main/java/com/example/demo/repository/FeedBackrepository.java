package com.example.demo.repository;

import com.example.demo.model.Feedback;
import com.example.demo.model.GroceryItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FeedBackrepository extends MongoRepository<Feedback, String> {
    public long count();
}
