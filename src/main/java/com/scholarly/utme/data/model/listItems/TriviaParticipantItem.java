package com.scholarly.utme.data.model.listItems;

public class TriviaParticipantItem {
    private String imageUrl;
    private String name;
    private int points;

    public TriviaParticipantItem(String imageUrl, String name, int points) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.points = points;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }
}

