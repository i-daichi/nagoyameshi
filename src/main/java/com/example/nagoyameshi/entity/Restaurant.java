package com.example.nagoyameshi.entity;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "restaurants")
@Data
public class Restaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "restaurant_id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "image_name")
	private String imageName;

	@Column(name = "description")
	private String description;

	@Column(name = "price")
	private Integer price;

	@Column(name = "seats")
	private Integer seats;

	@Column(name = "postal_code")
	private String postalCode;

	@Column(name = "address")
	private String address;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "category")
	private String category;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<RestaurantCategory> categories;

	@Column(name = "regular_holiday")
	private String regularHoliday;

	@OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<RestaurantHoliday> holidays = new ArrayList<>();

	@Column(name = "business_hours")
	private String businessHours;

	@Column(name = "created_at", insertable = false, updatable = false)
	private Timestamp createdAt;

	@Column(name = "updated_at", insertable = false, updatable = false)
	private Timestamp updatedAt;

	@Column(name = "opening_time")
	private LocalTime openingTime;

	@Column(name = "closing_time")
	private LocalTime closingTime;

	public Restaurant() {
	}

	public Restaurant(RestaurantRegisterForm form) {
		this.name = form.getName();
		this.description = form.getDescription();
		this.price = form.getPrice();
		this.seats = form.getSeats();
		this.postalCode = form.getPostalCode();
		this.address = form.getAddress();
		this.phoneNumber = form.getPhoneNumber();
		this.openingTime = form.getOpenTime().toLocalTime();
		this.closingTime = form.getCloseTime().toLocalTime();
	}

	public Restaurant(RestaurantEditForm form) {
		this.id = form.getId();
		this.name = form.getName();
		this.description = form.getDescription();
		this.price = form.getPrice();
		this.seats = form.getSeats();
		this.postalCode = form.getPostalCode();
		this.address = form.getAddress();
		this.phoneNumber = form.getPhoneNumber();
		this.openingTime = form.getOpenTime().toLocalTime();
		this.closingTime = form.getCloseTime().toLocalTime();
	}

	// コンストラクタ、ゲッター、セッター
    public List<RestaurantHoliday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<RestaurantHoliday> holidays) {
        this.holidays = holidays;
    }

    public void addHoliday(RestaurantHoliday holiday) {
        holidays.add(holiday);
    }

    public void removeHoliday(RestaurantHoliday holiday) {
        holidays.remove(holiday);
    }

	public List<RestaurantCategory> getCategories(){
		return this.categories;
	}

	public void setCategories(List<RestaurantCategory> categories){
		this.categories = categories;
	}

	public void addCategory(RestaurantCategory category){
		categories.add(category);
	}

	public void removeCategory(RestaurantCategory category){
		categories.remove(category);
	}
}
