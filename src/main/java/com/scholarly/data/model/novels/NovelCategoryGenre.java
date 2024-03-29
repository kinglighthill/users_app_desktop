package com.scholarly.data.model.novels;

import java.util.Objects;

public class NovelCategoryGenre {
    private int categoryId;
    private int genreId;
    private String category;
    private String genre;

    public NovelCategoryGenre(int categoryId, int genreId, String category, String genre) {
        this.categoryId = categoryId;
        this.genreId = genreId;
        this.category = category;
        this.genre = genre;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getGenreId() {
        return genreId;
    }

    public String getCategory() {
        return category;
    }

    public String getGenre() {
        return genre;
    }

    public String getTitle() {
        return category + " " + genre;
    }

    public String getDescription(int novelCount) {
        String description;
        if (novelCount == 1) {
            if (Objects.equals(genre, "Poetry")) {
                description = "Poem";
            } else {
                description = genre;
            }
        } else {
            if (Objects.equals(genre, "Poetry")) {
                description = "Poems";
            } else {
                description = genre + "s";
            }
        }
        return novelCount + " "+ description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NovelCategoryGenre that = (NovelCategoryGenre) o;
        return categoryId == that.categoryId && genreId == that.genreId && category.equals(that.category) &&
                genre.equals(that.genre);
    }

    @Override
    public int hashCode() {
        return categoryId + genreId + category.hashCode() + genre.hashCode();
    }
}


