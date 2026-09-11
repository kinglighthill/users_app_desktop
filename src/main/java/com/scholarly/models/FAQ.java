package com.scholarly.models;

import java.util.ArrayList;

public class FAQ {
    private String title;
    private ArrayList<FaqItem> faqItems;

    public FAQ(String title, ArrayList<FaqItem> faqItems) {
        this.title = title;
        this.faqItems = faqItems;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<FaqItem> getFaqItems() {
        return faqItems;
    }
}
