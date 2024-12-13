package com.example.nagoyameshi.dto;

public class GenreCategoryDTO {
    private String genreName;
    private String categoryName;

    public GenreCategoryDTO(String genreName, String categoryName) {
        this.genreName = genreName;
        this.categoryName = categoryName;
    }

    public String getGenreName() {
        return genreName;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
