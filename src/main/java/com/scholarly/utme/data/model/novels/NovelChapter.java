package com.scholarly.utme.data.model.novels;

public class NovelChapter {
    private int id;
    private int position;
    private String title;
    private String description;
    private int chapterCategoryId;
    private int novelId;
    private int order;
    private boolean free;

    private String chapterHeading;

    public NovelChapter(int id, int position, String title, String description, int chapterCategoryId, int novelId, int order, boolean free) {
        this.id = id;
        this.position = position;
        this.title = title;
        this.description = description;
        this.chapterCategoryId = chapterCategoryId;
        this.novelId = novelId;
        this.order = order;
        this.free = free;
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

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public String getChapterHeading() {
        return chapterHeading;
    }

    public void setChapterHeading(String chapterHeading) {
        this.chapterHeading = chapterHeading;
    }
}
