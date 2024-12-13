package com.example.nagoyameshi.helper;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.example.nagoyameshi.entity.RestaurantHoliday;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.RestaurantHolidayService;
import com.example.nagoyameshi.service.WeekdayService;

@Component
public class AdminRestaurantHelper {

    private final CategoryService categoryService;
    private final WeekdayService weekdayService;
    private final RestaurantHolidayService restaurantHolidayService;

    public AdminRestaurantHelper(
        CategoryService categoryService,
        WeekdayService weekdayService,
        RestaurantHolidayService restaurantHolidayService
    ) {
        this.categoryService = categoryService;
        this.weekdayService = weekdayService;
        this.restaurantHolidayService = restaurantHolidayService;
    }

    public void prepareEditPage(Integer restaurantId, Model model, RestaurantEditForm restaurantEditForm) {
        // 定休日取得
        List<RestaurantHoliday> restaurantHolidays = restaurantHolidayService.findByRestaurantId(restaurantId);

        // 必要なデータをモデルに追加
        model.addAttribute("restaurantEditForm", restaurantEditForm);
        model.addAttribute("restaurantHolidays", restaurantHolidays); // 設定済みの定休日
    }

    public void addHolidayData(Model model) {
        model.addAttribute("weekdays", weekdayService.findAll()); // 曜日マスタリスト
    }

    public void addMinuteIntervals(Model model) {
        model.addAttribute("minutes",
            Arrays.asList(0, 15, 30, 45)
                .stream()
                .map(m -> String.format("%02d", m))
                .collect(Collectors.toList())
        );
    }

    public void addTimeData(Model model) {
        model.addAttribute("hours",
            IntStream.range(0, 24)
                .mapToObj(i -> String.format("%02d", i))
                .collect(Collectors.toList())
        );
    }

    public void addCategoryData(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories()); // カテゴリリスト
    }
}
