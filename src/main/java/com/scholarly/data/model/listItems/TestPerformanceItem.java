package com.scholarly.data.model.listItems;

public class TestPerformanceItem {

    private String title;
    private String date;
    private double result;
    private String subjects;

    public TestPerformanceItem(String title, String date, double result, String subjects) {
        this.title = title;
        this.date = date;
        this.result = result;
        this.subjects = subjects;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public double getResult() {
        return result;
    }

    public String getSubjects() {
        return subjects;
    }
}
