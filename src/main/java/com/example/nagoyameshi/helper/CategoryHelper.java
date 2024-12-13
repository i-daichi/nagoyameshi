package com.example.nagoyameshi.helper;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;
import com.example.nagoyameshi.service.GenreService;

@Component
public class CategoryHelper {
    private final GenreService genreService;

    public CategoryHelper(GenreService genreService) {
        this.genreService = genreService;
    }

    public void AddCategoryDetails(Model model) {
        model.addAttribute("categoryRegisterForm", new CategoryRegisterForm());
        model.addAttribute("genres", genreService.getAllGenres());
    }

    public void SalvageCategoryDetails(Model model, CategoryRegisterForm categoryRegisterForm) {
        model.addAttribute("categoryRegisterForm", categoryRegisterForm);
        model.addAttribute("genres", genreService.getAllGenres());
    }

    public void AddCategoryDetails(Model model, CategoryEditForm categoryEditForm) {
        model.addAttribute("categoryEditForm", categoryEditForm);
        model.addAttribute("genres", genreService.getAllGenres());
    }
}
