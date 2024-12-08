package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaidInputForm {

    @NotBlank(message = "カード名義人（姓）を入力してください。")
    private String creditFirstName;
    
    @NotBlank(message = "カード名義人（名）を入力してください。")
    private String creditLastName;

    @NotBlank(message = "カード番号を入力してください。")
    private String creditNumber;
        
    @NotBlank(message = "有効期間（年）を入力してください。")
    private String creditYear;
    
    @NotBlank(message = "有効期間（月）を入力してください。")
    private String creditMonth;
    
    @NotBlank(message = "セキュリティコードを入力してください。")
    private String creditSecurityCode;

}
