package com.example.demo.model;


import io.github.kaiso.relmongo.annotation.CascadeType;
import io.github.kaiso.relmongo.annotation.FetchType;
import io.github.kaiso.relmongo.annotation.JoinProperty;
import io.github.kaiso.relmongo.annotation.OneToMany;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document("Place")
public class Place {
    @Id
    private String id;
    private String photos;
    private String IconPhotos;
    private long date;
    private double[] location = {0.0,0.0};
    private String description;
    private float cover;
    private String businessName;
    private String dbbusinessName;
    private String category;
    private String city;
    private String address;
    private String county;
    private int capacity;
    private String phoneNumber;
    private int currentCapacity = 0;
    private boolean importantPlace;

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @OneToMany(fetch= FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinProperty(name="events")
    private List<Event> events;

    public Place() {
        super();

    }
    /*
     public Place(String image, long date, double[] location, String description,float cover,String businessName,String city,String address,String county,int capacity,int currentCapacity,boolean importantPlace,String category,List<Event> events) {
        super();
     //   this.id = id;
        this.date = date;
        this.image = image;
        this.location = location;
        this.currentCapacity = currentCapacity;
        this.cover = cover;
        this.businessName = businessName;
        this.city = city;
        this.address = address;
        this.county = county;
        this.capacity = capacity;
        this.description = description;
        this.importantPlace = importantPlace;
        this.category = category;
        this.events = events;
    }
     */
    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double[] getLocation() {
        return location;
    }
    @Transient
    public String getPhotosImagePath() {
        if (photos == null || id == null) return null;

        return "/user-photos/" + id + "/" + photos;
    }
    public String getIconPhotos() {
        return IconPhotos;
    }
    public String getDbbusinessName() {
        return dbbusinessName;
    }

    public void setDbbusinessName(String dbbusinessName) {
        this.dbbusinessName = dbbusinessName;
    }
    public void setIconPhotos(String iconPhotos) {
        IconPhotos = iconPhotos;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void setLocation(double[] location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getCover() {
        return cover;
    }

    public void setCover(float cover) {
        this.cover = cover;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCurrentCapacity() {
        return currentCapacity;
    }

    public void setCurrentCapacity(int currentCapacity) {
        this.currentCapacity = currentCapacity;
    }

    public boolean isImportantPlace() {
        return importantPlace;
    }

    public void setImportantPlace(boolean importantPlace) {
        this.importantPlace = importantPlace;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
