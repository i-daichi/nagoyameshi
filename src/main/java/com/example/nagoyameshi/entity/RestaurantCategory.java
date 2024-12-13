package com.example.nagoyameshi.entity;

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
@Table(name = "restaurant_categories")
@Data
public class RestaurantCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_category_id")
    private Integer id;

    @Column(name = "restaurant_id",insertable = false, updatable = false)
    private Integer restaurantId;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "category_id", insertable = false, updatable = false)
    private Integer categoryId;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public RestaurantCategory(){

    }

    public RestaurantCategory(Integer restaurantId, Integer categoryId) {
        this.restaurantId = restaurantId;
        this.categoryId = categoryId;
    }

    public RestaurantCategory(Integer restaurantId, Restaurant restaurant, Integer categoryId, Category category) {
        this.restaurantId = restaurantId;
        this.restaurant = restaurant;
        this.categoryId = categoryId;
        this.category = category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
