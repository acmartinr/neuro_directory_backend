package com.example.demo.model;


import org.springframework.data.mongodb.core.mapping.Document;

@Document("config")
public class Config {
    public String getMin_version() {
        return min_version;
    }

    public void setMin_version(String min_version) {
        this.min_version = min_version;
    }

    private String min_version;

    public Config(){
        super();
    }
}
