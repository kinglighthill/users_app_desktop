package com.scholarly.utme.data.model.newDb.contentType;

public enum ContentType {
    TEXT("text"),
    HTML("html"),
    WEB_VIEW("webview"),
    TABLE("table"),
    ORDERED_LIST("ol"),
    UNORDERED_LIST("ul"),
    IMAGE("image"),
    VIDEO("video"),
    AUDIO("audio"),
    CBT("cbt");

    private String typeText;

    ContentType(String typeText) {
        this.typeText = typeText;
    }

    public String getTypeText() {
        return typeText;
    }
}
