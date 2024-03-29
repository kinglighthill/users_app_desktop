package com.scholarly.data.model.novels;

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

    public String getTimeText() {
        if (novel.getChaptersCount() > 30) {
            int totalMins = novel.getChaptersCount()*2;
            int div = totalMins/60;
            int rem = totalMins%60;
            return div + " hour(s) " + rem + "mins";
        } else {
            return novel.getChaptersCount() * 2 + " mins";
        }
    }
}