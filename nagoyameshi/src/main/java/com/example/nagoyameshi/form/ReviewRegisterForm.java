package com.example.nagoyameshi.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewRegisterForm {

    private Integer restaurantId;
    
    private Integer userId;    
        
    private Integer score;  
        
    private String comment;
}
