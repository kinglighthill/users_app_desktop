package com.scholarly.utme.data.model.newDb.contentType;

import com.google.gson.Gson;
import com.scholarly.utme.data.model.newDb.Section;
import com.scholarly.utme.data.model.newDb.contentType.audio.AudioViewType;
import com.scholarly.utme.data.model.newDb.contentType.cbt.CBTViewType;
import com.scholarly.utme.data.model.newDb.contentType.html.HtmlViewType;
import com.scholarly.utme.data.model.newDb.contentType.image.ImageViewType;
import com.scholarly.utme.data.model.newDb.contentType.orderedList.OrderedListViewType;
import com.scholarly.utme.data.model.newDb.contentType.table.TableViewType;
import com.scholarly.utme.data.model.newDb.contentType.tablehh.TableHHViewType;
import com.scholarly.utme.data.model.newDb.contentType.text.TextViewType;
import com.scholarly.utme.data.model.newDb.contentType.unorderedList.UnorderedListViewType;
import com.scholarly.utme.data.model.newDb.contentType.video.VideoViewType;
import com.scholarly.utme.data.model.newDb.contentType.webview.WebViewType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ContentViewTypes {
    TEXT(1,"text", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, TextViewType.class);
    }),
    HTML(2, "html", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, HtmlViewType.class);
    }),
    WEB_VIEW(3, "webview", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, WebViewType.class);
    }),
    TABLE(4, "table", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, TableViewType.class);
    }),
    TABLE_HH(5, "tableHH", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, TableHHViewType.class);
    }),
    ORDERED_LIST(6, "ol", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, OrderedListViewType.class);
    }),
    UNORDERED_LIST(7, "ul", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, UnorderedListViewType.class);
    }),
    IMAGE(8, "image", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, ImageViewType.class);
    }),
    VIDEO(9, "video", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, VideoViewType.class);
    }),
    AUDIO(10, "audio", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, AudioViewType.class);
    }),
    CBT(11, "cbt", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, CBTViewType.class);
    });

    private int id;
    private String typeText;
    private Convert converter;

    ContentViewTypes(int id, String typeText, Convert converter) {
        this.id = id;
        this.typeText = typeText;
        this.converter = converter;
    }

    public String getTypeText() {
        return typeText;
    }

    public ContentViewType getContentType(String content) {
        return converter.convert(content);
    }

    public static ContentViewType convert(Section section) {
        List<ContentViewTypes> contentViewTypesList = Arrays.stream(ContentViewTypes.values()).collect(Collectors.toList());

        for (int i = 0; i < contentViewTypesList.size(); i++) {
            if (contentViewTypesList.get(i).id == section.getContentViewTypeId()) {
                return contentViewTypesList.get(i).getContentType(section.getContent());
            }
        }

        return null;
    }

    private interface Convert {
        public ContentViewType convert(String content);
    }
}
