package com.example.nagoyameshi.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.dto.GenreCategoryDTO;
import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Genre;
import com.example.nagoyameshi.repository.GenreRepository;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    // ジャンル一覧を取得
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    // IDでジャンルを取得
    public Optional<Genre> getGenreById(Integer id) {
        return genreRepository.findById(id);
    }

    // ジャンルを追加
    public Genre addGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    // ジャンルを更新
    public Genre updateGenre(Integer id, Genre genre) {
        if (genreRepository.existsById(id)) {
            genre.setId(id);
            return genreRepository.save(genre);
        } else {
            throw new IllegalArgumentException("ジャンルが存在しません");
        }
    }

    // ジャンルを削除
    public void deleteGenre(Integer id) {
        genreRepository.deleteById(id);
    }

    public Map<String, List<String>> getGenreCategoryMap() {
        List<Genre> genres = genreRepository.findAllWithCategories();
        Map<String, List<String>> genreCategoryMap = new LinkedHashMap<>();

        for (Genre genre : genres) {
            List<String> categoryNames = genre.getCategories()
                    .stream()
                    .map(Category::getName)
                    .collect(Collectors.toList());
            genreCategoryMap.put(genre.getName(), categoryNames);
        }
        return genreCategoryMap;
    }

    public List<GenreCategoryDTO> getGenreCategoryDropdownList() {
        List<Genre> genres = genreRepository.findAllWithCategories();
        List<GenreCategoryDTO> dropdownList = new ArrayList<>();

        for (Genre genre : genres) {
            for (Category category : genre.getCategories()) {
                dropdownList.add(new GenreCategoryDTO(genre.getName(), category.getName()));
            }
        }

        return dropdownList;
    }
}