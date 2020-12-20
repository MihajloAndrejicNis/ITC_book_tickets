package com.example.rezervacijakarata.datamodels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailedEvent {

    private int id;
    private String address;
    private double lat;
    @SerializedName("long")
    private double welcome10Long;
    private String start;
    private String end;
    private String sport;
    private int price;
    private int tickets;
    private int available_tickets;
    private List<Team> teams;

    public DetailedEvent(int id, String address, double lat, double welcome10Long, String start, String end, String sport, int price, int tickets, int available_tickets, List<Team> teams) {
        this.id = id;
        this.address = address;
        this.lat = lat;
        this.welcome10Long = welcome10Long;
        this.start = start;
        this.end = end;
        this.sport = sport;
        this.price = price;
        this.tickets = tickets;
        this.available_tickets = available_tickets;
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

    public int getPrice() {
        return price;
    }

    public int getTickets() {
        return tickets;
    }

    public int getAvailable_tickets() {
        return available_tickets;
    }

    public List<Team> getTeams() {
        return teams;
    }
}
