package com.scholarly.utme.data.model.novels;

public class NovelModel {
    private Novel novel;
    private String division;

    public NovelModel(Novel novel, String division) {
        this.novel = novel;
        this.division = division;
    }

    public Novel getNovel() {
        return novel;
    }

    public String getDivision() {
        return division;
    }

    public String getChapterText() {
        if (novel.getChaptersCount() > 1) {
            return novel.getChaptersCount() + " " + division + "s";
        } else {
            return novel.getChaptersCount() + " " + division;
        }
    }
}