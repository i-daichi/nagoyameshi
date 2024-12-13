package com.example.nagoyameshi.controller;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.nagoyameshi.entity.User;
import com.example.nagoyameshi.form.UserEditForm;
import com.example.nagoyameshi.form.UserEditPaidForm;
import com.example.nagoyameshi.repository.UserRepository;
import com.example.nagoyameshi.security.UserDetailsImpl;
import com.example.nagoyameshi.service.UserService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.param.ChargeCreateParams;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;

@Controller
@RequestMapping("/user")
public class UserController {

	private final UserRepository userRepository; // Userリポジトリ
	private final UserService userService; // ユーザー関連サービス
	@Value("${stripe.webhook-secret}")
	private String stripeApiKey;

	@Value("${stripe.api-key}")
	private String stripePublicKey;

	// コンストラクタでUserRepositoryとUserServiceをDI
	public UserController(UserRepository userRepository, UserService userService) {
		this.userRepository = userRepository;
		this.userService = userService;
	}

	// ユーザー情報を表示するインデックスページ
	@GetMapping
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		User user = userRepository.findUserById(userDetailsImpl.getUser().getId()); // ログインユーザーの情報を取得

		model.addAttribute("user", user); // ユーザー情報をビューに渡す

		return "user/show"; // user/indexテンプレートを返す
	}

	// ユーザー情報編集ページの表示
	@GetMapping("/edit")
	public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		// ユーザー情報を取得し、UserEditFormにセット
		User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());
		UserEditForm userEditForm = new UserEditForm(user.getId(), user.getName(), user.getFurigana(),
				user.getPostalCode(), user.getAddress(), user.getPhoneNumber(), user.getEmail());

		model.addAttribute("userEditForm", userEditForm); // 編集フォームにユーザー情報を渡す

		return "user/edit"; // user/editテンプレートを返す
	}

	// ユーザー情報更新処理
	@PostMapping("/update")
	public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		// メールアドレスが変更されており、かつすでに登録されている場合、エラーメッセージを追加
		if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
			bindingResult.addError(
					new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。"));
		}

		// バリデーションエラーがあればフォームを再表示
		if (bindingResult.hasErrors()) {
			return "user/edit";
		}

		// ユーザー情報の更新処理
		userService.update(userEditForm);
		redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");

		return "redirect:/user"; // 更新後にユーザーの詳細ページにリダイレクト
	}

	// 無料会員から有料会員への変更画面に遷移
	@GetMapping("/changepaid")
	public String changepaid(
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			Model model,
			RedirectAttributes redirectAttributes) {
		// ログイン中のユーザー情報を元にUserEditPaidFormを作成
		UserEditPaidForm up = new UserEditPaidForm(userDetailsImpl.getUser().getId(), "");

		model.addAttribute("userEditPaidForm", up); // フォームデータをビューに渡す
		model.addAttribute("stripePublicKey", stripePublicKey); // 公開キーをビューに渡す

		return "user/changepaid"; // 有料会員変更画面を返す
	}

	@PostMapping("/charge")
	public String handlePayment(@RequestParam("stripeToken") String stripeToken,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes) {
		try {
			var user = userDetailsImpl.getUser();
			Stripe.apiKey = stripeApiKey; // シークレットキーを設定

			// Customerの取得 or 作成
			String customerId = user.getStripe_customer_id();
			Customer customer;

			if (customerId == null || customerId.isEmpty()) {
				CustomerCreateParams customerParams = CustomerCreateParams.builder()
						.setEmail(user.getEmail())
						.setName(user.getName())
						.build();
				customer = Customer.create(customerParams);
				userService.updateStripeCustomerId(user.getId(), customer.getId());
			} else {
				customer = Customer.retrieve(customerId);
			}

			// PaymentMethodの作成とアタッチ
			PaymentMethod paymentMethod = PaymentMethod.create(
					Map.of("type", "card", "card", Map.of("token", stripeToken)));
			paymentMethod.attach(Map.of("customer", customer.getId()));

			// PaymentIntentの作成
			PaymentIntentCreateParams paymentIntentParams = PaymentIntentCreateParams.builder()
					.setAmount(30000L) // 300円（単位はセント）
					.setCurrency("jpy")
					.setCustomer(customer.getId())
					.setPaymentMethod(paymentMethod.getId())
					.setConfirm(true) // 即時決済を確認
					.setOffSession(true) // オフセッション決済（必要に応じて）
					.build();

			PaymentIntent paymentIntent = PaymentIntent.create(paymentIntentParams);
			System.out.println("PaymentIntent成功: " + paymentIntent.getId());

			// ステータスを有料に変更
			updateUserRole(userDetailsImpl, 3); // 3は有料会員のロールID
			redirectAttributes.addFlashAttribute("successMessage", "有料プランへの登録が完了しました。");

		} catch (StripeException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage", "決済に失敗しました。もう一度お試しください。");
			return "redirect:/user/changepaid";
		}

		return "redirect:/";
	}

	// 有料会員から無料会員への変更画面に遷移
	@GetMapping("/changefree")
	public String changefree(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {
		// ログイン中のユーザー情報を元にUserEditPaidFormを作成
		UserEditPaidForm up = new UserEditPaidForm(userDetailsImpl.getUser().getId(), "");
		model.addAttribute("userEditPaidForm", up); // フォームデータをビューに渡す

		return "user/changefree"; // 無料会員変更画面を返す
	}

	// 会員ステータス（有料/無料）変更処理
	@PostMapping("/editpaid")
	public String editPaid(
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			@ModelAttribute @Validated UserEditPaidForm userEditPaidForm,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {
		System.out.println("editpaid######" + userDetailsImpl.getUser().getId()); // デバッグ用ログ
		System.out.println("bindingResult.hasErrors():" + bindingResult); // デバッグ用ログ

		// バリデーションエラーがあれば有料会員変更画面に戻す
		if (bindingResult.hasErrors()) {
			return "user/changepaid";
		}

		// ユーザーの会員ステータスを変更
		User updatedUser = userService.updatePaid(userDetailsImpl.getUser().getId());
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(updatedUser.getRole().getName());

		// 新しいUserDetailsImplを作成
		UserDetailsImpl updatedUserDetails = new UserDetailsImpl(updatedUser, authorities);
		downgradeToFree(userDetailsImpl, redirectAttributes);

		// 新しい認証情報を作成
		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
				updatedUserDetails, // 新しいUserDetails
				null, // パスワードはnullでもOK（すでに認証されているため）
				updatedUserDetails.getAuthorities() // 新しいAuthorities
		);

		SecurityContextHolder.getContext().setAuthentication(newAuth);
		redirectAttributes.addFlashAttribute("successMessage", "無料プランへの変更が完了しました。");

		return "redirect:/";
	}

	/**
	 * 無料会員への変更処理
	 */
	private String downgradeToFree(UserDetailsImpl userDetailsImpl, RedirectAttributes redirectAttributes) {
		updateUserRole(userDetailsImpl, 1); // 2は無料会員のロールID
		redirectAttributes.addFlashAttribute("successMessage", "無料プランへの変更が完了しました。");

		return "redirect:/";
	}

	/**
	 * ユーザーのロールを更新する処理
	 */
	private void updateUserRole(UserDetailsImpl userDetailsImpl, int roleId) {
		User updatedUser = userService.updatePaid(userDetailsImpl.getUser().getId());
		Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(updatedUser.getRole().getName());

		UserDetailsImpl updatedUserDetails = new UserDetailsImpl(updatedUser, authorities);

		// 新しい認証情報をセット
		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
				updatedUserDetails,
				null,
				updatedUserDetails.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(newAuth);
	}

	// 企業情報ページ
	@GetMapping("/company")
	public String company() {
		return "auth/company"; // 企業情報ページを返す
	}

	// サブスクリプション（有料会員）ページ
	@GetMapping("/changecard")
	public String subscription(Model model, @AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
		String customerId = userDetailsImpl.getUser().getStripe_customer_id();
		try {
			Stripe.apiKey = stripeApiKey; // シークレットキーを設定
			Customer customer = Customer.retrieve(customerId);
			List<PaymentMethod> paymentMethods = PaymentMethod.list(
					Map.of("customer", customerId, "type", "card")).getData();

			if (!paymentMethods.isEmpty()) {
				PaymentMethod card = paymentMethods.get(0);
				String brand = card.getCard().getBrand();
				String last4 = card.getCard().getLast4();
				model.addAttribute("cardBrand", brand);
				model.addAttribute("cardLast4", last4);
			}

		} catch (StripeException e) {
			e.printStackTrace();
			model.addAttribute("error", "カード情報の取得に失敗しました");
		}

		model.addAttribute("stripePublicKey", stripePublicKey); // 公開キーをビューに渡す

		return "user/changecard"; // サブスクリプションページを返す
	}

	@PostMapping("/update-card")
	public String updateCard(@RequestParam("stripeToken") String stripeToken,
			@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
			RedirectAttributes redirectAttributes) {
		String customerId = userDetailsImpl.getUser().getStripe_customer_id();

		try {
			Stripe.apiKey = stripeApiKey; // シークレットキーを設定
			Customer customer = Customer.retrieve(customerId);

			// PaymentMethodをCustomerにアタッチする
			PaymentMethod paymentMethod = PaymentMethod.retrieve(stripeToken);
			paymentMethod.attach(PaymentMethodAttachParams.builder().setCustomer(customerId).build());

			// Customerのデフォルト支払い方法を更新
			CustomerUpdateParams params = CustomerUpdateParams.builder()
					.setInvoiceSettings(CustomerUpdateParams.InvoiceSettings.builder()
							.setDefaultPaymentMethod(stripeToken)
							.build())
					.build();

			Customer updatedCustomer = customer.update(params);

			redirectAttributes.addFlashAttribute("successMessage", "カード情報を変更しました。");
			return "redirect:/";

		} catch (StripeException e) {
			e.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage", "決済に失敗しました。もう一度お試しください。");
			return "redirect:/user/changecard";
		}
	}

}
