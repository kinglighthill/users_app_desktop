package com.scholarly.utme.data.model.listItems;

public class UpdateItem {
    private String imageUrl;
    private String title;
    private String date;
    private String details;

    public UpdateItem(String imageUrl, String title, String date, String details) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.date = date;
        this.details = details;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }
}
