package com.scholarly.utme.data.model.listItems;

public class VideoItem {
    private String videoUrl;
    private String title;
    private int time;
    private int rating;
    private String description;

    public VideoItem(String videoUrl, String title, int time, int rating, String description) {
        this.videoUrl = videoUrl;
        this.title = title;
        this.time = time;
        this.rating = rating;
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public int getTime() {
        return time;
    }

    public int getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }
}
