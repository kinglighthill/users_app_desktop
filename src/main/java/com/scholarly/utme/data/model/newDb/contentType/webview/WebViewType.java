package com.scholarly.utme.data.model.newDb.contentType.webview;

import com.scholarly.utme.data.model.newDb.contentType.ContentType;
import com.scholarly.utme.data.model.newDb.contentType.Title;

public class WebViewType extends ContentType {
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
