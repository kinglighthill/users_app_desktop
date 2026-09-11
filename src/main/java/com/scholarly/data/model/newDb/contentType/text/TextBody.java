package com.scholarly.data.model.newDb.contentType.text;

public class TextBody {
    private String[] bodyType;
    private String text;
    private Link link;

    public TextBody(String[] bodyType, String text, Link link) {
        this.bodyType = bodyType;
        this.text = text;
        this.link = link;
    }

    public String[] getBodyType() {
        return bodyType;
    }

    public String getText() {
        return text;
    }

    public Link getLink() {
        return link;
    }
}
