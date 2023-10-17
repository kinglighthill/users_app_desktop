package com.scholarly.utme.data.model;

public class FreeContent {
    private int id;
    private int objectiveSubjectId;
    private int theorySubjectId;
    private int yearId;
    private int topicId;
    private int chapterId;

    public FreeContent(int id, int objectiveSubjectId, int theorySubjectId, int yearId, int topicId, int chapterId) {
        this.id = id;
        this.objectiveSubjectId = objectiveSubjectId;
        this.theorySubjectId = theorySubjectId;
        this.yearId = yearId;
        this.topicId = topicId;
        this.chapterId = chapterId;
    }

    public int getId() {
        return id;
    }

    public int getObjectiveSubjectId() {
        return objectiveSubjectId;
    }

    public int getTheorySubjectId() {
        return theorySubjectId;
    }

    public int getYearId() {
        return yearId;
    }

    public int getTopicId() {
        return topicId;
    }

    public int getChapterId() {
        return chapterId;
    }
}
