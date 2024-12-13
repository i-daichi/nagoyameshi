package com.example.nagoyameshi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.helper.RestaurantHelper;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;
import com.example.nagoyameshi.service.GenreService;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.ReviewService;

@Controller
@RequestMapping("/restaurants")
public class RestaurantController {
	private final RestaurantRepository restaurantRepository;
	private final RestaurantService restaurantService;
	private final ReviewService reviewService;
	private final FavoriteService favoriteService;
	private final GenreService genreService;

	public RestaurantController(
			RestaurantRepository restaurantRepository,
			RestaurantService restaurantService,
			ReviewService reviewService,
			FavoriteService favoriteService,
			GenreService genreService) {
		this.restaurantRepository = restaurantRepository;
		this.restaurantService = restaurantService;
		this.reviewService = reviewService;
		this.favoriteService = favoriteService;
		this.genreService = genreService;
	}

	@GetMapping
	public String index(@RequestParam(required = false) String keyword,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) Integer price,
			@RequestParam(required = false) String order,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		var restaurantPage = restaurantService.getRestaurants(keyword, category, price, order, pageable);
		Map<String, List<String>> genreCategoryMap = genreService.getGenreCategoryMap();

		model.addAttribute("genreCategoryMap", genreCategoryMap);
		model.addAttribute("restaurantPage", restaurantPage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("selectedCategory", category);
		model.addAttribute("price", price);
		model.addAttribute("order", order);

		return "restaurants/index";
	}



	@GetMapping("/{id}")
	public String show(@PathVariable Integer id, Model model,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {

		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		User user = null;

		if (userDetailsImpl != null) {
			user = userDetailsImpl.getUser();
		}

		RestaurantHelper helper = new RestaurantHelper(reviewService, favoriteService);
		helper.AddRestaurantDetails(model, restaurant, user, new ReservationInputForm());

		return "restaurants/show";
	}

}
