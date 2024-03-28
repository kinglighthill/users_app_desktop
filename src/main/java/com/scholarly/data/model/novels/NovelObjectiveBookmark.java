package com.scholarly.data.model.novels;

public class NovelObjectiveBookmark {
    private int id;
    private int novelId;
    private int chapterId;
    private int questionId;

    public NovelObjectiveBookmark(int id, int novelId, int chapterId, int questionId) {
        this.id = id;
        this.novelId = novelId;
        this.chapterId = chapterId;
        this.questionId = questionId;
    }

    public int getId() {
        return id;
    }

    public int getNovelId() {
        return novelId;
    }

    public int getChapterId() {
        return chapterId;
    }

    public int getQuestionId() {
        return questionId;
    }
}
