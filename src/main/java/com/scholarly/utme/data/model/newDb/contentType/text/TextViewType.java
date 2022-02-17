package com.scholarly.utme.data.model.newDb.contentType.text;

import com.scholarly.utme.data.model.newDb.contentType.ContentType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

public class TextViewType extends ContentType {
    private Title title;
    private TextBody body;

    public TextViewType(Title title, TextBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public TextBody getBody() {
        return body;
    }
}
