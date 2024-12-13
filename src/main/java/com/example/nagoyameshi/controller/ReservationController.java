package com.example.nagoyameshi.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.Reservation;
import com.example.nagoyameshi.entity.Restaurant;
import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.ReservationInputForm;
import com.example.nagoyameshi.form.ReservationRegisterForm;
import com.example.nagoyameshi.helper.RestaurantHelper;
import com.example.nagoyameshi.repository.ReservationRepository;
import com.example.nagoyameshi.repository.RestaurantRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.FavoriteService;
import com.example.nagoyameshi.service.ReservationService;
import com.example.nagoyameshi.service.ReviewService;

import jakarta.validation.Valid;

@Controller
public class ReservationController {

	// リポジトリとサービスをインジェクション
	private final ReservationRepository reservationRepository;
	private final RestaurantRepository restaurantRepository;
	private final ReservationService reservationService;
	private final ReviewService reviewService;
	private final FavoriteService favoriteService;

	// コンストラクタで依存関係を注入
	public ReservationController(ReservationRepository reservationRepository, RestaurantRepository restaurantRepository,
			ReservationService reservationService, ReviewService reviewService, FavoriteService favoriteService) {
		this.reservationRepository = reservationRepository;
		this.restaurantRepository = restaurantRepository;
		this.reservationService = reservationService;
		this.reviewService = reviewService;
		this.favoriteService = favoriteService;
	}

	// ユーザーの予約一覧を表示するメソッド
	@GetMapping("/reservations")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) {
		// ログインしているユーザーを取得
		User user = userDetailsImpl.getUser();

		// ユーザーの予約情報を取得し、ページングを適用
		Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);

		// モデルに予約情報を追加
		model.addAttribute("reservationPage", reservationPage);

		// 予約一覧ページを表示
		return "reservations/index";
	}

	// 予約入力ページを表示し、入力内容を確認するメソッド
	@GetMapping("/restaurants/{id}/reservations/input")
	public String confirm(@PathVariable Integer id,
			@ModelAttribute @Valid ReservationInputForm reservationInputForm,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model) {

		// 指定されたレストランを取得
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		// 入力フォームから予約人数を取得
		Integer numberOfPeople = reservationInputForm.getNumberOfPeople();
		// レストランの席数を取得
		Integer seats = restaurant.getSeats();

		User user = null;
		if (userDetailsImpl != null) {
			user = userDetailsImpl.getUser();
		}

		// 予約人数が席数を超えないかチェック
		if (numberOfPeople != null) {
			if (!reservationService.isWithinSeats(numberOfPeople, seats)) {
				// エラー: 予約人数が席数を超えている場合
				bindingResult
						.addError(
								new FieldError(bindingResult.getObjectName(),
										"numberOfPeople", "人数が定員を超えています。"));
			}
		}

		// バリデーションエラーがあった場合
		if (bindingResult.hasErrors()) {
			for (ObjectError iter : bindingResult.getAllErrors()) {
				System.out.println(iter.getDefaultMessage());
			}
			// モデルにレストラン情報とエラーメッセージをセットして、再度入力フォームを表示
			redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);
			model.addAttribute("errorMessage", "予約内容に不備があります。");
			RestaurantHelper helper = new RestaurantHelper(reviewService, favoriteService);
			helper.AddRestaurantDetails(model, restaurant, user, reservationInputForm);
			return "restaurants/show";
		}

		// エラーがなければ、リダイレクトで予約内容を確認ページに渡す
		redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);

		// 予約確認ページへリダイレクト
		return "redirect:/restaurants/{id}/reservations/confirm";
	}

	// 予約内容の確認ページを表示するメソッド
	@GetMapping("/restaurants/{id}/reservations/confirm")
	public String confirm(@PathVariable Integer id,
			@ModelAttribute ReservationInputForm reservationInputForm,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model) {

		// レストランとユーザー情報を取得
		Restaurant restaurant = restaurantRepository.getReferenceById(id);
		User user = userDetailsImpl.getUser();

		// 入力フォームから予約日を取得
		LocalDate rDate = reservationInputForm.getReservationDate();

		// 予約登録フォームに必要な情報をセット
		ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(restaurant.getId(), user.getId(),
				rDate.toString(), reservationInputForm.getNumberOfPeople());

		// モデルにレストラン情報と予約登録フォームをセット
		model.addAttribute("restaurant", restaurant);
		model.addAttribute("reservationRegisterForm", reservationRegisterForm);

		// 予約確認ページを表示
		return "reservations/confirm";
	}

	// 予約作成処理を行うメソッド
	@PostMapping("/restaurants/{id}/reservations/create")
	public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {
		// サービスを通じて予約を作成
		reservationService.create(reservationRegisterForm);

		// 予約完了後、予約一覧ページにリダイレクト
		return "redirect:/reservations?reserved";
	}
}
