package com.scholarly.data.model.newDb.contentType.orderedList;

import com.scholarly.data.model.newDb.contentType.ContentViewType;
import com.scholarly.data.model.newDb.contentType.Title;

public class OrderedListViewType extends ContentViewType {
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
