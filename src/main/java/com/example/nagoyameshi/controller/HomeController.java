package com.example.nagoyameshi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.service.GenreService;

@Controller
public class HomeController {
	private final RestaurantRepository restaurantRepository;
	private final GenreService genreService;

	public HomeController(
			RestaurantRepository restaurantRepository,
			GenreService genreService) {
		this.restaurantRepository = restaurantRepository;
		this.genreService = genreService;
	}

	@GetMapping("/")
	public String index(Model model) {
		List<Restaurant> newRestaurants = restaurantRepository.findTop10ByOrderByCreatedAtDesc();
		Map<String, List<String>> genreCategoryMap = genreService.getGenreCategoryMap();
		model.addAttribute("genreCategoryMap", genreCategoryMap);
		model.addAttribute("newRestaurants", newRestaurants);
		return "index";
	}

}
