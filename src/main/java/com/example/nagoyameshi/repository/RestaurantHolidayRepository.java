package com.example.nagoyameshi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.nagoyameshi.entity.RestaurantHoliday;

@Repository
public interface RestaurantHolidayRepository extends JpaRepository<RestaurantHoliday, Integer> {
    List<RestaurantHoliday> findByRestaurantId(Integer restaurantId);
    void deleteByRestaurantId(Integer restaurant_id);
}
