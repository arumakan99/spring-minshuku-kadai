package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.form.ReservationInputForm;
import com.example.samuraitravel.repository.HouseRepository;

@Controller
@RequestMapping("/houses")
public class HouseController {
	private final HouseRepository houseRepository;        

	public HouseController(HouseRepository houseRepository) {
		this.houseRepository = houseRepository;            
	}     

	@GetMapping
	public String index(@RequestParam(name = "keyword", required = false) String keyword,
			@RequestParam(name = "area", required = false) String area,
			@RequestParam(name = "price", required = false) Integer price,
			@RequestParam(name = "order", required = false) String order,
			@PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
			Model model) 
	{
		// 物件データのページを取得する
		Page<House> housePage;

		// パラメータに応じてhouseRepositoryから物件データを取得し検索結果をhousePageに格納
		if (keyword != null && !keyword.isEmpty()) {
			// キーワードが指定されている場合
			if (order != null && order.equals("priceAsc")) {
				// ソート順が価格の昇順の場合
				housePage = houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
			} else {
				// それ以外の場合は作成日時の降順でソート
				housePage = houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
			}            
		} else if (area != null && !area.isEmpty()) {
			// エリアが指定されている場合
			if (order != null && order.equals("priceAsc")) {
				// ソート順が価格の昇順の場合
				housePage = houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
			} else {
				// それ以外の場合は作成日時の降順でソート
				housePage = houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
			}            
		} else if (price != null) {
			// 価格が指定されている場合
			if (order != null && order.equals("priceAsc")) {
				// ソート順が価格の昇順の場合
				housePage = houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
			} else {
				// それ以外の場合は作成日時の降順でソート
				housePage = houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
			}            
		} else {
			// 条件が指定されていない場合
			if (order != null && order.equals("priceAsc")) {
				// ソート順が価格の昇順の場合
				housePage = houseRepository.findAllByOrderByPriceAsc(pageable);
			} else {
				// それ以外の場合は作成日時の降順でソート
				housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);   
			}            
		}          

		// 検索結果をモデルに追加
		model.addAttribute("housePage", housePage);
		model.addAttribute("keyword", keyword);
		model.addAttribute("area", area);
		model.addAttribute("price", price);  
		model.addAttribute("order", order);

		return "houses/index";
	}

	@GetMapping("/{id}")
	public String show(@PathVariable(name = "id")Integer id, Model model) {
		// 物件IDに基づいて物件情報を取得
		House house = houseRepository.getReferenceById(id);

		// 物件情報と予約入力フォームをモデルに追加
		model.addAttribute("house", house);
		model.addAttribute("reservationInputForm", new ReservationInputForm());

		return "houses/show";
	}
}