package com.scholarly.utme.data.model.newDb.contentViewType;

import com.google.gson.annotations.SerializedName;
import com.scholarly.utme.data.model.newDb.contentType.ContentViewType;

public class CBTViewType extends ContentViewType {
    @SerializedName("subject_id")
    private int subjectId;
    @SerializedName("year_id")
    private int yearId;
    @SerializedName("question_id")
    private int questionId;

    public CBTViewType(int subjectId, int yearId, int questionId) {
        this.subjectId = subjectId;
        this.yearId = yearId;
        this.questionId = questionId;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public int getYearId() {
        return yearId;
    }

    public int getQuestionId() {
        return questionId;
    }
}
