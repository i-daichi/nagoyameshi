package com.example.nagoyameshi.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.RestaurantCategory;
import com.example.nagoyameshi.entity.RestaurantHoliday;
import com.example.nagoyameshi.valueObject.HourMinute;

public class RestaurantDTO {
    private Integer id;
    private String name;
    private String imageName;
    private String description;
    private Integer price;
    private Integer seats;
    private String postalCode;
    private String address;
    private String phoneNumber;
    private String category;
    private String regularHoliday;
    private String businessHours;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private HourMinute openingTime;
    private HourMinute closingTime;
    private List<RestaurantCategory> categories = new ArrayList<>();;
    private List<RestaurantHoliday> holidays = new ArrayList<>();

    public RestaurantDTO(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.imageName = restaurant.getImageName();
        this.description = restaurant.getDescription();
        this.price = restaurant.getPrice();
        this.seats = restaurant.getSeats();
        this.postalCode = restaurant.getPostalCode();
        this.address = restaurant.getAddress();
        this.phoneNumber = restaurant.getPhoneNumber();
        this.category = restaurant.getCategory();
        this.regularHoliday = restaurant.getRegularHoliday();
        this.businessHours = restaurant.getBusinessHours();
        this.createdAt = restaurant.getCreatedAt();
        this.updatedAt = restaurant.getUpdatedAt();
        this.openingTime = new HourMinute(restaurant.getOpeningTime());
        this.closingTime = new HourMinute(restaurant.getClosingTime());
        this.categories = restaurant.getCategories();
        this.holidays = restaurant.getHolidays();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSeats() {
        return seats;
    }

    public void setSeats(Integer seats) {
        this.seats = seats;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegularHoliday() {
        return regularHoliday;
    }

    public void setRegularHoliday(String regularHoliday) {
        this.regularHoliday = regularHoliday;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public HourMinute getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(HourMinute openingTime) {
        this.openingTime = openingTime;
    }

    public HourMinute getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(HourMinute closingTime) {
        this.closingTime = closingTime;
    }

    public void setCategories(List<RestaurantCategory> categories) {
        this.categories = categories;
    }

    public List<RestaurantCategory> getCategories() {
        return categories;
    }

    public void setHolidays(List<RestaurantHoliday> holidays){
        this.holidays = holidays;
    }

    public List<RestaurantHoliday> getHolidays(){
        return holidays;
    }

    // メソッド
    public String getBusinessTime() {
        return openingTime.toString() + "~" + closingTime.toString();
    }

    public  String categoriesToString() {
        if (categories == null || categories.isEmpty()) {
            return "なし";  // リストが空またはnullなら "なし" を返す
        }
        return categories.stream()
                .map(category -> category.getCategory().getName())
                .collect(Collectors.joining("、"));
    }

    public String holidaysToString() {
        if (holidays == null || holidays.isEmpty()) {
            return "なし";  // リストが空またはnullなら "なし" を返す
        }
        return holidays.stream()
                .map(h -> h.getWeekday().getName())
                .collect(Collectors.joining("、"));
    }
}
