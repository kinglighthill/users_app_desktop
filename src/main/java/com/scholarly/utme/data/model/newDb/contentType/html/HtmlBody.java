package com.scholarly.utme.data.model.newDb.contentType.html;

public class HtmlBody {
    private String[] bodyType;
    private String text;
    private Link link;

    public HtmlBody(String[] bodyType, String text, Link link) {
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
