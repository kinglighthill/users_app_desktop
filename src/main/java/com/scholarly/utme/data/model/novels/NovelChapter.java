package com.scholarly.utme.data.model.novels;

public class NovelChapter {

    private int id;
    private int position;
    private String title;
    private String description;
    private String details;
    private int category;
    private int isRead;
    private int novelId;

    public NovelChapter(int id, int position, String title, String description, String details, int category, int isRead, int novelId) {
        this.id = id;
        this.position = position;
        this.title = title;
        this.description = description;
        this.details = details;
        this.category = category;
        this.isRead = isRead;
        this.novelId = novelId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public int getNovelId() {
        return novelId;
    }

    public void setNovelId(int novelId) {
        this.novelId = novelId;
    }
}
