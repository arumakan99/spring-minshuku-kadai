let maxDate = new Date();
// maxDateに3か月後の日付を代入
maxDate = maxDate.setMonth(maxDate.getMonth() + 3);

// カレンダー選択範囲を設定
flatpickr('#fromCheckinDateToCheckoutDate', {
	mode: "range", // 範囲選択モードに設定
	locale: 'ja', // 日本語ローカライゼーションを使用
	minDate: 'today', // 今日以降の日付を選択可能に設定
	maxDate: maxDate // 3か月後までの日付を選択可能に設定
});