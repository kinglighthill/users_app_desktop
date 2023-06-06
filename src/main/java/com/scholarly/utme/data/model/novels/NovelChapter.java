package com.scholarly.utme.data.model.novels;

public class NovelChapter {

    private int id;
    private int position;
    private String title;
    private String description;
    private int chapterCategoryId;
    private int novelId;
    private int order;

    public NovelChapter(int id, int position, String title, String description, int chapterCategoryId, int novelId, int order) {
        this.id = id;
        this.position = position;
        this.title = title;
        this.description = description;
        this.chapterCategoryId = chapterCategoryId;
        this.novelId = novelId;
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getChapterCategoryId() {
        return chapterCategoryId;
    }

    public int getNovelId() {
        return novelId;
    }

    public int getOrder() {
        return order;
    }
}
