package com.scholarly.utme.data.model;

public class SubSection {

    private int id;
    private String content;
    private int sectionId;
    private int isWebView;
    private String imageUrl;

    public SubSection(int id, String content, int sectionId, int isWebView, String imageUrl) {
        this.id = id;
        this.content = content;
        this.sectionId = sectionId;
        this.isWebView = isWebView;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public int getIsWebView() {
        return isWebView;
    }

    public void setIsWebView(int isWebView) {
        this.isWebView = isWebView;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
