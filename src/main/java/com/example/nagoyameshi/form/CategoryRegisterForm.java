package com.example.nagoyameshi.form;

import com.example.nagoyameshi.entity.Genre;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryRegisterForm {
    @NotBlank(message = "カテゴリ名を入力してください。")
    private String name;
    @NotNull(message = "ジャンルを選択してください")
    private Genre genre;

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
