package com.scholarly.data.model.newDb.contentType.unorderedList;

import com.scholarly.data.model.newDb.contentType.ContentViewType;
import com.scholarly.data.model.newDb.contentType.Title;

public class UnorderedListViewType extends ContentViewType {
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
