package com.scholarly.data.model.newDb;

import com.google.gson.Gson;
import com.scholarly.data.model.newDb.contentType.ContentViewType;
import com.scholarly.data.model.newDb.contentViewType.*;

import java.util.Arrays;
import java.util.List;

public enum ContentViewTypes {
    HEADER(20, "header", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, HeaderViewType.class);
    }),
    PARAGRAPH(21, "paragraph", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, ParagraphViewType.class);
    }),
    SIMPLE_IMAGE(24, "simple-image", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, SimpleImageViewType.class);
    }),
    CBT(26, "cbt", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, CBTViewType.class);
    }),
    LATEX_MATH(29, "latex-math", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, LatexMathViewType.class);
    }),
    LIST(30, "list", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, ListViewType.class);
    }),
    NESTED_LIST(31, "nested-list", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, NestedListViewType.class);
    }),
    TABLE(32, "table", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, TableViewType.class);
    }),
    REFERENCE(38, "reference", content -> {
        Gson gson = new Gson();
        return gson.fromJson(content, ReferenceViewType.class);
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
        List<ContentViewTypes> contentViewTypesList = Arrays.stream(ContentViewTypes.values()).toList();

        for (ContentViewTypes contentViewTypes : contentViewTypesList) {
            if (contentViewTypes.id == section.getContentViewTypeId()) {
                return contentViewTypes.getContentType(section.getContent());
            }
        }

        return null;
    }

    public static ContentViewType convert(SyllabusSection section) {
        List<ContentViewTypes> contentViewTypesList = Arrays.stream(ContentViewTypes.values()).toList();

        return contentViewTypesList.get(0).getContentType(section.getContent());

    }

    private interface Convert {
        ContentViewType convert(String content);
    }
}
