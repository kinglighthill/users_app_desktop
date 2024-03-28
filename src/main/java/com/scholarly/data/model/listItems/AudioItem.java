package com.scholarly.data.model.listItems;

public class AudioItem {
    private String audioUrl;
    private String title;
    private int time;
    private double rating;
    private String description;

    public AudioItem(String audioUrl, String title, int time, double rating, String description) {
        this.audioUrl = audioUrl;
        this.title = title;
        this.time = time;
        this.rating = rating;
        this.description = description;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getTime() {
        return time;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }
}
