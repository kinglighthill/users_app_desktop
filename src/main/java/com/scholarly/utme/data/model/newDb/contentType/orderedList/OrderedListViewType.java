package com.scholarly.utme.data.model.newDb.contentType.orderedList;

import com.scholarly.utme.data.model.newDb.contentType.ContentType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

public class OrderedListViewType extends ContentType {
    private Title title;
    private OrderedListBody body;

    public OrderedListViewType(Title title, OrderedListBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public OrderedListBody getBody() {
        return body;
    }
}
