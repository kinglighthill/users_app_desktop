package com.scholarly.utme.data.model.newDb.contentType.html;

import com.scholarly.utme.data.model.newDb.contentType.ContentType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

public class HtmlViewType extends ContentType {
    private Title title;
    private HtmlBody body;

    public HtmlViewType(Title title, HtmlBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public HtmlBody getBody() {
        return body;
    }
}
