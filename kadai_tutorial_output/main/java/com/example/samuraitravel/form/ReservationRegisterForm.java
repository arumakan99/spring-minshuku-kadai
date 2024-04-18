package com.example.samuraitravel.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReservationRegisterForm {    
	// 予約対象の民宿ID
	private Integer houseId;

	// 予約したユーザーのID
	private Integer userId;    

	// チェックイン日の文字列
	private String checkinDate;    

	// チェックアウト日の文字列
	private String checkoutDate;    

	// 宿泊人数
	private Integer numberOfPeople;

	// 予約金額
	private Integer amount;    
}