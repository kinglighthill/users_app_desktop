package com.scholarly.data.model.newDb.contentType.webview;

import com.scholarly.data.model.newDb.contentType.ContentViewType;
import com.scholarly.data.model.newDb.contentType.Title;

public class WebViewType extends ContentViewType {
    private Title title;
    private WebViewBody body;

    public WebViewType(Title title, WebViewBody body) {
        this.title = title;
        this.body = body;
    }

    public Title getTitle() {
        return title;
    }

    public WebViewBody getBody() {
        return body;
    }
}
