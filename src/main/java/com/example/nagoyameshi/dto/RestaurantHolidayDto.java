package com.example.nagoyameshi.dto;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.RestaurantHoliday;
import com.example.nagoyameshi.entity.Weekday;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantHolidayDto {
    private Weekday weekday;

    public RestaurantHolidayDto(RestaurantHoliday holiday){
        this.weekday = holiday.getWeekday();
    }

    public RestaurantHoliday toRestaurantHoliday(Restaurant restaurant) {
        RestaurantHoliday holiday = new RestaurantHoliday();
        holiday.setRestaurant(restaurant);
        holiday.setWeekday(weekday);
        return holiday;
    }
}
