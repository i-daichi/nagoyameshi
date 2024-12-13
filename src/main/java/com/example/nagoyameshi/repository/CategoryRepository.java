package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nagoyameshi.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // 必要なメソッドを追加（例: nameでカテゴリを取得するなど）
    Category findByName(String name);

    List<Category> findByNameContaining(String name); // nameの部分一致検索

    Page<Category> findByNameLike(String string, Pageable pageable);
}
