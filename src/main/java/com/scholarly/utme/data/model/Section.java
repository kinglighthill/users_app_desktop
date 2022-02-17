package com.scholarly.utme.data.model;

public class Section {

    private int id;
    private String content;
    private int topicId;
    private int isWebView;
    private String imageUrl;

    public Section(int id, String content, int topicId, int isWebView, String imageUrl) {
        this.id = id;
        this.content = content;
        this.topicId = topicId;
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

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
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
