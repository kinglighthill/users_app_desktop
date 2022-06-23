package com.scholarly.utme.data.model.listItems;

public class TriviaParticipantItem {
    private int rank;
    private String imageUrl;
    private String name;
    private int points;

    public TriviaParticipantItem(int rank, String imageUrl, String name, int points) {
        this.rank = rank;
        this.imageUrl = imageUrl;
        this.name = name;
        this.points = points;
    }

    public int getRank() {
        return rank;
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

