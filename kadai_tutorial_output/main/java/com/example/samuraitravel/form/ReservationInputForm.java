package com.example.samuraitravel.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
	// チェックイン日とチェックアウト日の入力値を保持するフィールド
	@NotBlank(message = "チェックイン日とチェックアウト日を選択してください。")
	private String fromCheckinDateToCheckoutDate;    

	// 宿泊人数の入力値を保持するフィールド
	@NotNull(message = "宿泊人数を入力してください。")
	@Min(value = 1, message = "宿泊人数は1人以上に設定してください。")
	private Integer numberOfPeople; 

	// チェックイン日を取得するメソッド
	public LocalDate getCheckinDate() {
		// " から "を区切り文字に使用してチェックイン日とチェックアウト日を分割
		String[] checkinDateAndCheckoutDate = getFromCheckinDateToCheckoutDate().split(" から ");
		// チェックイン日をLocalDate型に変換して返す
		return LocalDate.parse(checkinDateAndCheckoutDate[0]);
	}

	// チェックアウト日を取得するメソッド
	public LocalDate getCheckoutDate() {
		// " から "を区切り文字に使用してチェックイン日とチェックアウト日を分割
		String[] checkinDateAndCheckoutDate = getFromCheckinDateToCheckoutDate().split(" から ");
		// チェックアウト日をLocalDate型に変換して返す
		return LocalDate.parse(checkinDateAndCheckoutDate[1]);
	}
}