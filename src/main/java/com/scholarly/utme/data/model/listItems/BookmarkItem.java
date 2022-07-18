package com.scholarly.utme.data.model.listItems;

public class BookmarkItem {
    private String imageUrl;
    private String title;
    private String item;
    private String category;

    public BookmarkItem(String imageUrl, String title, String item, String category) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.item = item;
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getItem() {
        return item;
    }

    public String getCategory() {
        return category;
    }
}
