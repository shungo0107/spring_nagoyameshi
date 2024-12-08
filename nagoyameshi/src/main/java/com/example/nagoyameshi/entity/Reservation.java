package com.example.nagoyameshi.entity;

import java.sql.Timestamp;
import java.time.LocalDate;

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
@Table(name = "reservations")
@Data
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant; 
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;     
    
    @Column(name = "reserve_date ")
    private LocalDate ReserveDate ;
    
    @Column(name = "number_of_people")
    private Integer numberOfPeople; 
    
    @Column(name = "amount")
    private Integer amount;     
    
    @Column(name = "create_date", insertable = false, updatable = false)
    private Timestamp createDate;
    
    @Column(name = "update_date", insertable = false, updatable = false)
    private Timestamp updateDate;    
}