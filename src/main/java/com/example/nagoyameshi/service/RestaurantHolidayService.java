package com.example.nagoyameshi.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.RestaurantCategory;
import com.example.nagoyameshi.entity.RestaurantHoliday;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.repository.RestaurantHolidayRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;

@Service
public class RestaurantHolidayService {
    private final RestaurantHolidayRepository restaurantHolidayRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantHolidayService(
            RestaurantHolidayRepository restaurantHolidayRepository,
            RestaurantRepository restaurantRepository) {
        this.restaurantHolidayRepository = restaurantHolidayRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public List<RestaurantHoliday> findByRestaurantId(Integer restaurantId) {
        return restaurantHolidayRepository.findByRestaurantId(restaurantId);
    }

    public List<RestaurantHoliday> findAllById(List<Integer> idList) {
        return restaurantHolidayRepository.findAllById(idList);
    }

    @Transactional
    public void deleteByRestaurantId(Integer id) {
        Restaurant restaurant = restaurantRepository.getReferenceById(id);
        restaurantHolidayRepository.deleteByRestaurantId(id);
        restaurant.setCategories(new ArrayList<RestaurantCategory>());

        restaurantRepository.save(restaurant);
    }

    @Transactional
    public void insert(Restaurant restaurant, RestaurantRegisterForm form) {
        List<RestaurantHoliday> holidays = form.getHolidays().stream()
                .map(weekday ->
                    new RestaurantHoliday(
                        restaurant.getId(),
                        restaurant,
                        weekday.getId(),
                        weekday)
                )
                .collect(Collectors.toList());

        restaurantHolidayRepository.saveAll(holidays);
    }
}
