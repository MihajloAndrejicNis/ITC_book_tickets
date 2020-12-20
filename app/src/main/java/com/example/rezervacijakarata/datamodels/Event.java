package com.example.rezervacijakarata.datamodels;

import java.util.List;

public class Event {

    private int id;
    private String address;
    private double lat;
    private double welcome10Long;
    private String start;
    private String end;
    private String sport;
    private List<Team> teams;

    public Event(int id, String address, double lat, double welcome10Long, String start, String end, String sport, List<Team> teams) {
        this.id = id;
        this.address = address;
        this.lat = lat;
        this.welcome10Long = welcome10Long;
        this.start = start;
        this.end = end;
        this.sport = sport;
        this.teams = teams;
    }

    public int getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getWelcome10Long() {
        return welcome10Long;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getSport() {
        return sport;
    }

    public List<Team> getTeams() {
        return teams;
    }
}
