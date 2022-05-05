package com.scholarly.utme.data.model.newDb.contentType.cbt;

import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

public class CBTViewType extends ContentViewType {
    private Title title;
    private CBTBody body;

    public CBTViewType(Title title, CBTBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public CBTBody getBody() {
        return body;
    }
}
