package com.scholarly.utme.data.model.listItems;

public class AppItem {
    private String name;
    private String imageUrl;
    private String description;

    public AppItem(String name, String imageUrl, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
