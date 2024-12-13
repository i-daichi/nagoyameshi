package com.example.nagoyameshi.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.dto.RestaurantDTO;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.form.RestaurantEditForm;
import com.example.nagoyameshi.form.RestaurantRegisterForm;
import com.example.nagoyameshi.helper.AdminRestaurantHelper;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.service.CategoryService;
import com.example.nagoyameshi.service.RestaurantCategoryService;
import com.example.nagoyameshi.service.RestaurantHolidayService;
import com.example.nagoyameshi.service.RestaurantService;
import com.example.nagoyameshi.service.WeekdayService;

@Controller
@RequestMapping("/admin/restaurants")
public class AdminRestaurantController {
	private final RestaurantRepository restaurantRepository;
	private final RestaurantService restaurantService;
	private final CategoryService categoryService;
	private final WeekdayService weekdayService;
	private final RestaurantHolidayService restaurantHolidayService;
	private final RestaurantCategoryService restaurantCategoryService;

	public AdminRestaurantController(
			RestaurantRepository restaurantRepository,
			RestaurantService restaurantService,
			CategoryService categoryService,
			WeekdayService weekdayService,
			RestaurantHolidayService restaurantHolidayService,
			RestaurantCategoryService restaurantCategoryService) {
		this.restaurantRepository = restaurantRepository;
		this.restaurantService = restaurantService;
		this.categoryService = categoryService;
		this.weekdayService = weekdayService;
		this.restaurantHolidayService = restaurantHolidayService;
		this.restaurantCategoryService = restaurantCategoryService;
	}

	@GetMapping
	public String index(
			Model model,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			@RequestParam(required = false) String keyword) {
		model.addAttribute("restaurantPage", restaurantService.getRestaurants(pageable, keyword));
		model.addAttribute("keyword", keyword);

		return "admin/restaurants/index";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable Integer id, Model model) {
		Restaurant restaurant = restaurantService.getReferenceById(id);

		model.addAttribute("restaurant", restaurant);

		return "admin/restaurants/show";
	}

	@GetMapping("/register")
	public String register(Model model) {
		var helper = new AdminRestaurantHelper(
				categoryService,
				weekdayService,
				restaurantHolidayService);
		helper.addCategoryData(model);
		helper.addHolidayData(model);
		helper.addTimeData(model);
		helper.addMinuteIntervals(model);

		model.addAttribute("restaurantRegisterForm", new RestaurantRegisterForm());
		return "admin/restaurants/register";
	}

	@PostMapping("/create")
	public String create(
			@ModelAttribute @Validated RestaurantRegisterForm restaurantRegisterForm,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			return "admin/restaurants/register";
		}

		restaurantRegisterForm.setCategories(categoryService.findAllById(restaurantRegisterForm.getCategoryIdList()));
		restaurantRegisterForm.setHolidays(weekdayService.findAllById(restaurantRegisterForm.getHolidayIdList()));

		var restaurant = restaurantService.create(restaurantRegisterForm);

		// 関連情報の挿入
		restaurantCategoryService.insert(restaurant, restaurantRegisterForm);
		restaurantHolidayService.insert(restaurant, restaurantRegisterForm);

		redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");

		return "redirect:/admin/restaurants";
	}

	@GetMapping("/{id}/edit")
	public String edit(@PathVariable Integer id, Model model) {
		Restaurant restaurant = restaurantService.getReferenceById(id);
		var restaurantEditForm = new RestaurantEditForm(restaurant);

		var helper = new AdminRestaurantHelper(
				categoryService,
				weekdayService,
				restaurantHolidayService);
		helper.prepareEditPage(id, model, restaurantEditForm);
		helper.addCategoryData(model);
		helper.addHolidayData(model);
		helper.addTimeData(model);
		helper.addMinuteIntervals(model);

		return "admin/restaurants/edit";
	}

	@PostMapping("/{id}/update")
	public String update(@ModelAttribute @Validated RestaurantEditForm restaurantEditForm,
			Model model, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {

		if (bindingResult.hasErrors()) {
			var helper = new AdminRestaurantHelper(
					categoryService,
					weekdayService,
					restaurantHolidayService);
			helper.prepareEditPage(restaurantEditForm.getId(), model, restaurantEditForm);
			helper.addCategoryData(model);
			helper.addHolidayData(model);
			helper.addTimeData(model);
			helper.addMinuteIntervals(model);
			return "admin/restaurants/edit";
		}

		restaurantEditForm.setCategories(categoryService.findAllById(restaurantEditForm.getCategoryIdList()));
		restaurantEditForm.setHolidays(weekdayService.findAllById(restaurantEditForm.getHolidayIdList()));

		// 関連情報の削除
		restaurantCategoryService.deleteByRestaurantId(restaurantEditForm.getId());
		restaurantHolidayService.deleteByRestaurantId(restaurantEditForm.getId());

		// レストラン情報の更新
		restaurantService.update(restaurantEditForm);

		redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");

		return "redirect:/admin/restaurants";
	}

	@PostMapping("/{id}/delete")
	public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
		restaurantRepository.deleteById(id);

		redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");

		return "redirect:/admin/restaurants";
	}
}
