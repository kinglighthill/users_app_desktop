package com.scholarly.utme.data.model.newDb;

public class NovelLastSession {
    private final int id;
    private final int chapterId;
    private final String chapterTitle;
    private final String userId;

    public NovelLastSession(int id, int chapterId, String chapterTitle, String userId) {
        this.id = id;
        this.chapterId = chapterId;
        this.chapterTitle = chapterTitle;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public int getChapterId() {
        return chapterId;
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public String getUserId() {
        return userId;
    }
}
