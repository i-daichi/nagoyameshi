package com.example.nagoyameshi.form;

import com.example.nagoyameshi.entity.Genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryEditForm {

    private Integer id;
    @NotBlank(message = "カテゴリ名を入力してください。")
    private String name;
    @NotNull(message = "ジャンルを選択してください")
    private Genre genre;

    public CategoryEditForm(Integer id, String name, Genre genre) {
        this.id = id;
        this.name = name;
        this.genre = genre;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
