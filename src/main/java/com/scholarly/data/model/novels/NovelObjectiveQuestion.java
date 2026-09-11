package com.scholarly.data.model.novels;

import com.scholarly.data.util.QuestionAnswer;
import com.scholarly.data.util.QuestionOption;

public class NovelObjectiveQuestion {
    private int id;
    private int novelId;
    private int chapterId;
    private int questionNumber;
    private String question;
    private QuestionOption optionA;
    private QuestionOption optionB;
    private QuestionOption optionC;
    private QuestionOption optionD;
    private QuestionOption optionE;
    private QuestionAnswer questionAnswer;
    private int questionDescriptionId;

    public NovelObjectiveQuestion(int id, int novelId, int chapterId, int questionNumber, String question, QuestionOption optionA, QuestionOption optionB, QuestionOption optionC, QuestionOption optionD, QuestionOption optionE, QuestionAnswer questionAnswer, int questionDescriptionId) {
        this.id = id;
        this.novelId = novelId;
        this.chapterId = chapterId;
        this.questionNumber = questionNumber;
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.optionE = optionE;
        this.questionAnswer = questionAnswer;
        this.questionDescriptionId = questionDescriptionId;
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

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getQuestion() {
        return question;
    }

    public QuestionOption getOptionA() {
        return optionA;
    }

    public QuestionOption getOptionB() {
        return optionB;
    }

    public QuestionOption getOptionC() {
        return optionC;
    }

    public QuestionOption getOptionD() {
        return optionD;
    }

    public QuestionOption getOptionE() {
        return optionE;
    }

    public QuestionAnswer getQuestionAnswer() {
        return questionAnswer;
    }

    public int getQuestionDescriptionId() {
        return questionDescriptionId;
    }
}
