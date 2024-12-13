package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.nagoyameshi.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    @Query("SELECT g FROM Genre g JOIN FETCH g.categories")
    List<Genre> findAllWithCategories();
}
