package com.example.nagoyameshi.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @Column(name = "name")
    private String name;
        
    @Column(name = "furigana")
    private String furigana;    
           
    @Column(name = "email")
    private String email;
        
    @Column(name = "password")
    private String password;    
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;   
    
    @Column(name = "enabled")
    private Boolean enabled;
    
    @Column(name = "paid_flg")
    private String paidFlg;    

    @Column(name = "credit_first_name")
    private String creditFirstName;    

    @Column(name = "credit_last_name")
    private String creditLastName;    

    @Column(name = "credit_number")
    private String creditNumber;    

    @Column(name = "credit_year")
    private String creditYear;    

    @Column(name = "credit_month")
    private String creditMonth; 
    
    @Column(name = "credit_secure_code")
    private String creditSecureCode;     
    
    @Column(name = "create_date", insertable = false, updatable = false)
    private Timestamp createDate;
    
    @Column(name = "update_date", insertable = false, updatable = false)
    private Timestamp updateDate;     

}