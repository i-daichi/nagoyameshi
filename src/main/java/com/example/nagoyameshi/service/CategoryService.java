package com.example.nagoyameshi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    // カテゴリの全件取得
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // カテゴリIDでの検索
    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    // 部分一致検索（名前で検索）
    public List<Category> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContaining(name);
    }

    public List<Category> findAllById(List<Integer> idList){
        return categoryRepository.findAllById(idList);
    }

    public Page<Category> getCategoryPages(Pageable pageable, String keyword) {
        Page<Category> categoryPage;
        if (keyword != null && !keyword.isEmpty()) {
            categoryPage = categoryRepository.findByNameLike("%" + keyword + "%", pageable);
        } else {
            categoryPage = categoryRepository.findAll(pageable);
        }
        return categoryPage;
    }

    // カテゴリの登録
    @Transactional
    public Category createCategory(String name) {
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    // カテゴリの更新
    @Transactional
    public Category updateCategory(Integer id, String name) {
        Optional<Category> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            Category category = categoryOpt.get();
            category.setName(name);
            return categoryRepository.save(category);
        }
        return null; // 見つからなかった場合
    }

    // カテゴリの削除
    @Transactional
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    public void create(CategoryRegisterForm categoryRegisterForm) {
        categoryRepository.save(new Category(categoryRegisterForm));
    }

    // カテゴリの削除
    public void update(CategoryEditForm categoryEditForm) {
        categoryRepository.save(new Category(categoryEditForm));
    }
}
