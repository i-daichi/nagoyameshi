package com.example.nagoyameshi.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.entity.Weekday;
import com.example.nagoyameshi.valueObject.HourMinute;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RestaurantRegisterForm {
	@NotBlank(message = "店舗名を入力してください。")
	private String name;

	private MultipartFile imageFile;

	@NotBlank(message = "説明を入力してください。")
	private String description;

	@NotNull(message = "価格帯を入力してください。")
	@Min(value = 100, message = "価格帯は100円以上に設定してください。")
	private Integer price;

	@NotBlank(message = "郵便番号を入力してください。")
	private String postalCode;

	@NotBlank(message = "住所を入力してください。")
	private String address;

	@NotBlank(message = "電話番号を入力してください。")
	private String phoneNumber;

	@NotNull(message = "開店時間を選択してください。")
	private HourMinute openTime;

	@NotNull(message = "閉店時間を選択してください。")
	private HourMinute closeTime;

	@NotNull(message = "座席数を入力してください。")
	private Integer seats;

	@NotNull(message = "カテゴリを選択してください。")
	private List<Integer> categoryIdList;

	private List<Integer> holidayIdList;

	private List<Category> categories;
    private List<Weekday> holidays;

	public RestaurantRegisterForm(){

	}
}
