package com.example.android.cookies.Entities;

import java.util.Date;

public class Event {
    private String artist;
    private String performance;
    private EventType type;
    private String id;
    private Date startTime;
    private String venueName;
    private String venueStreet;
    private String venueCity;

    public Event() { }

    public Event(String artist, String performance, EventType eventType) {
        this.artist = artist;
        this.performance = performance;
        type = eventType;
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

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
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
