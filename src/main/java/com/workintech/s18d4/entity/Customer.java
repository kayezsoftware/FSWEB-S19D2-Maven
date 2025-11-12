package com.workintech.s18d4.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*; // FetchType importunu doğrulayın
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer", schema = "fsweb")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ... (diğer alanlar: firstName, lastName, email, salary)
    private String firstName;
    private String lastName;
    private String email;
    private Double salary;


    // --- DÜZELTME BURADA ---
    // CascadeType.ALL (REMOVE dahil) ayarı SAHİP tarafa (JoinColumn olan taraf) eklenir.
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @JsonManagedReference
    private Address address;
    // --- DÜZELTME SONU ---

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Account> accounts = new ArrayList<>();
}
