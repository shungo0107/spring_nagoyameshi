package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetInputForm {
	
	@NotBlank(message = "パスワードを入力してください。")
	private String password;

	@NotBlank(message = "パスワード（確認用）を入力してください。")
	private String passwordConfirmation;

}
