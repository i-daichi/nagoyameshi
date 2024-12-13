package com.example.nagoyameshi.form;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReservationInputForm {
	@NotNull(message = "予約日を選択してください。")
	@FutureOrPresent(message = "予約日は未来の日付または今日の日付でなければなりません。") // 未来または今日の日付
	private LocalDate reservationDate;

	@NotNull(message = "人数を入力してください。")
	@Min(value = 1, message = "人数は1人以上に設定してください。")
	private Integer numberOfPeople;
}
