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
@Table(name = "restaurant_holidays")
@Data
public class RestaurantHoliday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_holiday_id", insertable = false, updatable = false)
    private Integer id;

    @Column(name = "restaurant_id", insertable = false, updatable = false)
    private Integer restaurant_id;
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(name = "weekday_id", insertable = false, updatable = false)
    private Integer weekday_id;
    @ManyToOne(optional = true)
    @JoinColumn(name = "weekday_id")
    private Weekday weekday;

    public RestaurantHoliday() {

    }

    public RestaurantHoliday(Integer restaurant_id, Integer weekday_id) {
        this.restaurant_id = restaurant_id;
        this.weekday_id = weekday_id;
    }

    public RestaurantHoliday(Integer restaurant_id, Restaurant restaurant, Integer weekday_id, Weekday weekday) {
        this.restaurant_id = restaurant_id;
        this.weekday_id = weekday_id;
        this.restaurant = restaurant;
        this.weekday = weekday;
    }
}
