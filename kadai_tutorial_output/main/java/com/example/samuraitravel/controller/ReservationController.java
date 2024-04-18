package com.example.samuraitravel.controller;

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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.Reservation;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.form.ReservationRegisterForm;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.repository.ReservationRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.ReservationService;
import com.example.samuraitravel.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReservationController {
	private final ReservationRepository reservationRepository;
	private final HouseRepository houseRepository;
	private final ReservationService reservationService;
    private final StripeService stripeService; 	

	// コンストラクタインジェクション
    public ReservationController(ReservationRepository reservationRepository, HouseRepository houseRepository, ReservationService reservationService, StripeService stripeService) { 
		this.reservationRepository = reservationRepository;
		this.houseRepository = houseRepository;
		this.reservationService = reservationService;
        this.stripeService = stripeService;
	}

	// 予約一覧ページを表示するメソッド
	@GetMapping("/reservations")
	public String index(@AuthenticationPrincipal UserDetailsImpl userDetails, @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, Model model) {
		// 認証されたユーザー情報を取得
		User user = userDetails.getUser();

		// ユーザーに関連する予約情報を作成日時の降順で取得
		Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);

		// ビューに予約情報を渡す
		model.addAttribute("reservationPage", reservationPage);

		// 予約一覧ページへ遷移
		return "reservations/index";
	}

	// 予約入力フォームページを表示するメソッド
	@GetMapping("/houses/{id}/reservations/input")
	public String input(@PathVariable(name = "id") Integer houseId, @ModelAttribute @Validated ReservationInputForm reservationInputForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
		// 民宿情報を取得
		House house = houseRepository.getReferenceById(houseId);
		Integer numberOfPeople = reservationInputForm.getNumberOfPeople();
		Integer capacity = house.getCapacity();

		// 定員を超える宿泊人数のチェック
		if (numberOfPeople != null) {
			if (!reservationService.isWithinCapacity(numberOfPeople, capacity)) {
				FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numberOfPeople", "宿泊人数が定員を超えています");
				bindingResult.addError(fieldError);
			}
		}

		// 入力エラーがある場合は詳細ページにエラーメッセージを表示
		if (bindingResult.hasErrors()) {
			model.addAttribute("house", house);
			model.addAttribute("errorMessage", "予約内容に不備があります");
			return "houses/show";
		}

		// 入力フォームをリダイレクト先に渡す
		redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);

		// 確認ページにリダイレクト
		return "redirect:/houses/{id}/reservations/confirm";
	}
	
	// 予約確認ページを表示するメソッド
	@GetMapping("/houses/{id}/reservations/confirm")
	public String confirm(@PathVariable(name = "id") Integer id, @ModelAttribute ReservationInputForm reservationInputForm, @AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletRequest httpServletRequest, Model model) {
		House house = houseRepository.getReferenceById(id);
		User user = userDetails.getUser();
                
		// チェックイン日とチェックアウト日を取得する
		LocalDate checkinDate = reservationInputForm.getCheckinDate();
		LocalDate checkoutDate = reservationInputForm.getCheckoutDate();

		// 宿泊料金を計算する
		Integer price = house.getPrice();
		Integer amount = reservationService.calculateAmount(checkinDate, checkoutDate, price);
        
		// 予約登録フォームを作成しモデルに追加
		ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(house.getId(), user.getId(), checkinDate.toString(), checkoutDate.toString(), reservationInputForm.getNumberOfPeople(), amount);
        
        // Stripeセッションを作成し、セッションIDを取得
        String sessionId = stripeService.createStripeSession(house.getName(), reservationRegisterForm, httpServletRequest);		
		
		model.addAttribute("house", house);
		model.addAttribute("reservationRegisterForm", reservationRegisterForm);
        model.addAttribute("sessionId", sessionId);
        
		return "reservations/confirm";
	}
	
	/*
    @PostMapping("/houses/{id}/reservations/create")
    public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {                
        reservationService.create(reservationRegisterForm);        
        
        return "redirect:/reservations?reserved";
    }
    */
}