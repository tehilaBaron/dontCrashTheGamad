package com.example.android_ex2.Models;

public class Record {
    private int points = 0;
    private String name = "";
    private double lat;
    private double lon;

    public Record(int points, String name, double lat, double lon) {
        this.points = points;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Record() {
    }
}
