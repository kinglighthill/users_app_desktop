package com.scholarly.utme.data.model.newDb.contentType.unorderedList;

import com.scholarly.utme.data.model.newDb.contentType.ContentType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

public class UnorderedListViewType extends ContentType {
    private Title title;
    private UnorderedListBody body;

    public UnorderedListViewType(Title title, UnorderedListBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public UnorderedListBody getBody() {
        return body;
    }
}
