package com.example.timerapplication;

public class timer {
    private int id;
    private String duration;
    private int hours;
    private int minutes;
    private int seconds;
    private String sound;
    private String endTime;

    public timer(int id, String duration, int hours, int minutes, int seconds, String sound, String endTime) {
        this.id = id;
        this.duration = duration;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.sound = sound;
        this.endTime = endTime;
    }

    // Getters and setters for each field

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
