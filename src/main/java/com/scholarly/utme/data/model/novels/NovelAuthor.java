package com.scholarly.utme.data.model.novels;

public class NovelAuthor {

    private int id;
    private String name;
    private int novelId;

    public NovelAuthor(int id, String name, int novelId) {
        this.id = id;
        this.name = name;
        this.novelId = novelId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNovelId() {
        return novelId;
    }

}
