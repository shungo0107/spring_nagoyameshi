package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ResetForm {
	
	@NotBlank(message = "メールアドレスを入力してください。")
	private String email;

}
