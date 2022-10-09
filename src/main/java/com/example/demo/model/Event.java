package com.example.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Document("Event")
public class Event {
    @Id
    private String id;
    private String photos;
    private String placeId;
    private String name;
    private String description;
    private String organizer;
    private boolean booking;
    private String strDate;
    private long date = 0;
    private String category;
    private float cover;

    public String getName() {
        return name;
    }
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getPhotos() {
        return photos;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setPhotos(String photos) {
        this.photos = photos;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getCover() {
        return cover;
    }

    public void setCover(float cover) {
        this.cover = cover;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        LocalDate ld = LocalDate.parse(strDate);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        Date date = Date.from(ld.atStartOfDay(defaultZoneId).toInstant());
        setDate(date.getTime());
        this.strDate = strDate;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public boolean isBooking() {
        return booking;
    }

    public void setBooking(boolean booking) {
        this.booking = booking;
    }
    /*
    public Event(String name,String description,long date,String category, float cover){
        super();
        this.name = name;
        this.description = description;
        this.date = date;
        this.category = category;
        this.cover = cover;
    }
 */
    public Event(){
        super();
    }
}
