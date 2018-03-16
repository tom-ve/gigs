package com.example.android.cookies.Entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Event implements Serializable {
    private String artist;
    private String performance;
    private String type;
    private long id;
    private Calendar startTime;
    private String venueName;
    private String venueStreet;
    private String venueCity;

    public Event() { }

    public Event(long id, String artist, String performance, String eventType, String venueName, String venueCity) {
        this.artist = artist;
        this.performance = performance;
        type = eventType;
        this.venueName = venueName;
        this.venueCity = venueCity;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueStreet() {
        return venueStreet;
    }

    public void setVenueStreet(String venueStreet) {
        this.venueStreet = venueStreet;
    }

    public String getVenueCity() {
        return venueCity;
    }

    public void setVenueCity(String venueCity) {
        this.venueCity = venueCity;
    }

    @Override
    public String toString() {
        return "Event{" +
                "artist='" + artist + '\'' +
                ", performance='" + performance + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
