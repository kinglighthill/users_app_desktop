package com.scholarly.utme.data.model.newDb;

import com.google.gson.Gson;
import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;
import com.scholarly.utme.data.model.newDb.contentViewType.HeaderViewType;
import com.scholarly.utme.data.model.newDb.contentViewType.ParagraphViewType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum ContentViewTypes {
    HEADER(20, "header", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, HeaderViewType.class);
    }),
    PARAGRAPH(21, "paragraph", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, ParagraphViewType.class);
    });

    private int id;
    private String type;
    private Convert converter;

    ContentViewTypes(int id, String type, Convert converter) {
        this.id = id;
        this.type = type;
        this.converter = converter;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public ContentViewType getContentType(String content) {
        return converter.convert(content);
    }

    public static ContentViewType convert(NoteSection section) {
        List<ContentViewTypes> contentViewTypesList = Arrays.stream(ContentViewTypes.values()).collect(Collectors.toList());

        for (int i = 0; i < contentViewTypesList.size(); i++) {
            if (contentViewTypesList.get(i).id == section.getContentViewTypeId()) {
                return contentViewTypesList.get(i).getContentType(section.getContent());
            }
        }

        return null;
    }

    private interface Convert {
        ContentViewType convert(String content);
    }
}
