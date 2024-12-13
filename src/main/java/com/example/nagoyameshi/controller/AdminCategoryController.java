package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;
import com.example.nagoyameshi.helper.CategoryHelper;
import com.example.nagoyameshi.repository.CategoryRepository;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.GenreService;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryController {

    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final GenreService genreService;

    public AdminCategoryController(CategoryRepository categoryRepository, CategoryService categoryService,
            GenreService genreService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.genreService = genreService;
    }

    // カテゴリ一覧を表示
    @GetMapping
    public String index(Model model,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String keyword) {

        model.addAttribute("categoryPage",categoryService.getCategoryPages(pageable, keyword));
        model.addAttribute("keyword", keyword);

        return "admin/categories/index"; // Thymeleaf テンプレート
    }

    // カテゴリ登録フォーム表示
    @GetMapping("/register")
    public String register(Model model) {
        CategoryHelper helper = new CategoryHelper(genreService);
        helper.AddCategoryDetails(model);
        return "admin/categories/register";
    }

    // カテゴリ登録処理
    @PostMapping("/create")
    public String create(
            Model model,
            @ModelAttribute @Validated CategoryRegisterForm categoryRegisterForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            CategoryHelper helper = new CategoryHelper(genreService);
            helper.SalvageCategoryDetails(model, categoryRegisterForm);
            return "admin/categories/register";
        }

        categoryService.create(categoryRegisterForm);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリを登録しました。");

        return "redirect:/admin/categories";
    }

    // カテゴリ編集フォーム表示
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Integer id, Model model) {
        Category category = categoryService.getCategoryById(id).get();
        CategoryEditForm categoryEditForm = new CategoryEditForm(
                category.getId(),
                category.getName(),
                genreService.getGenreById(category.getGenreId()).get());

        CategoryHelper helper = new CategoryHelper(genreService);
        helper.AddCategoryDetails(model, categoryEditForm);

        return "admin/categories/edit";
    }

    // カテゴリ編集処理
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated CategoryEditForm categoryEditForm,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            for (ObjectError error : bindingResult.getAllErrors()) {
                System.out.println(error.getDefaultMessage());
                System.err.println(error.getDefaultMessage());
            }
            return "admin/categories/edit";
        }

        categoryService.update(categoryEditForm);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリ情報を編集しました。");

        return "redirect:/admin/categories";
    }

    // カテゴリ削除処理
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        categoryService.deleteCategory(id);
        redirectAttributes.addFlashAttribute("successMessage", "カテゴリを削除しました。");

        return "redirect:/admin/categories";
    }
}
