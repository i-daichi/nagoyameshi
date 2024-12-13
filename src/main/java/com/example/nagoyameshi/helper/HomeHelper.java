package com.example.nagoyameshi.helper;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Genre;
import com.example.nagoyameshi.service.GenreService;

@Component
public class HomeHelper {
    private final GenreService genreService;

    public HomeHelper(GenreService genreService) {
        this.genreService = genreService;
    }

    public Map<String, List<String>> getGenreCategoryMap() {
        List<Genre> genres = genreService.getAllGenres();
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
}
