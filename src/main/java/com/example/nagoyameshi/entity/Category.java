package com.example.nagoyameshi.entity;

import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", columnDefinition = "INTEGER")
    private Integer id;

    @Column(name = "name", columnDefinition = "VARCHAR")
    private String name;

    @Column(name = "genre_id", insertable = false, updatable = false)
    private Integer genreId;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {
    }

    public Category(CategoryEditForm editForm) {
        id = editForm.getId();
        name = editForm.getName();
        genreId = editForm.getGenre().getId();
    }

    public Category(CategoryRegisterForm registerForm) {
        name = registerForm.getName();
        genreId = registerForm.getGenre().getId();
        genre = registerForm.getGenre();
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

    public void setGenreId(Integer genreId) {
        this.genreId = genreId;
    }

    public Integer getGenreId() {
        return genreId;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Genre getGenre() {
        return genre;
    }
}
