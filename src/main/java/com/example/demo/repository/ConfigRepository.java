package com.example.demo.repository;

import com.example.demo.model.Config;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigRepository extends MongoRepository<Config, String> {
    public long count();
}
