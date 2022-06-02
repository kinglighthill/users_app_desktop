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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNovelId() {
        return novelId;
    }

    public void setNovelId(int novelId) {
        this.novelId = novelId;
    }
}
