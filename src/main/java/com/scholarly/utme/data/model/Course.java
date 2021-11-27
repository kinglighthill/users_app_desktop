package com.scholarly.utme.data.model;

public class Course {
    private String name;
    private String imageURL;

    public Course(String name, String imageURL) {
        this.setName(name);
        this.setImageURL(imageURL);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
