package com.workintech.s18d4.entity;

import com.fasterxml.jackson.annotation.JsonBackReference; // EKLENDİ
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account", schema = "fsweb")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "money_amount")
    private Double moneyAmount;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    @JsonBackReference // EKLENDİ (Döngüyü burada kırar)
    private Customer customer;
}
