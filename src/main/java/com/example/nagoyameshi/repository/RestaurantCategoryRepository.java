package com.example.nagoyameshi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nagoyameshi.entity.RestaurantCategory;

@Repository
public interface RestaurantCategoryRepository extends JpaRepository<RestaurantCategory, Integer> {

    // レストランIDとカテゴリIDでレストランカテゴリのデータを取得
    RestaurantCategory findByRestaurantIdAndCategoryId(int restaurantId, int categoryId);

    void deleteByRestaurantId(Integer restaurant_id);
}
