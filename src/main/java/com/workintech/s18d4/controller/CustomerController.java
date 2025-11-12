package com.workintech.s18d4.controller;

import com.workintech.s18d4.dto.CustomerResponse;
import com.workintech.s18d4.entity.Address; // EKLENDİ
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AddressService; // EKLENDİ
import com.workintech.s18d4.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;
    private final AddressService addressService; // GEREKLİ: Adresi bulmak için eklendi.

    @GetMapping
    public List<Customer> findAll() {
        return customerService.findAll();
    }

    @GetMapping("/{id}")
    public Customer find(@PathVariable long id) {
        return customerService.find(id);
    }

    // --- BURASI DÜZELTİLDİ (POST) ---
    @PostMapping
    public CustomerResponse save(@RequestBody Customer customer) {
        // 1. Gelen JSON'daki 'address' nesnesini ve ID'sini kontrol et
        if (customer.getAddress() != null && customer.getAddress().getId() != 0) {

            // 2. ID'yi kullanarak Address'in tam (managed) halini DB'den çek
            Address realAddress = addressService.find(customer.getAddress().getId());

            if (realAddress != null) {
                // 3. Customer'a bu 'gerçek' adresi ata
                customer.setAddress(realAddress);
            } else {
                // (İsteğe bağlı) Adres bulunamazsa hata fırlatabiliriz
                throw new RuntimeException("Address with ID " + customer.getAddress().getId() + " not found.");
            }
        }

        // 4. Şimdi Customer'ı güvenle kaydet
        Customer savedCustomer = customerService.save(customer);
        return new CustomerResponse(savedCustomer.getId(), savedCustomer.getEmail(), savedCustomer.getSalary());
    }
    // --- DÜZELTME SONU (POST) ---


    // --- BURASI DÜZELTİLDİ (PUT) ---
    @PutMapping("/{id}")
    public Customer update(@RequestBody Customer customer, @PathVariable long id) {
        Customer existingCustomer = customerService.find(id);
        if (existingCustomer != null) {
            customer.setId(id); // ID'nin doğru ayarlandığından emin ol

            // POST'taki aynı 'detached entity' sorununu PUT için de çözmeliyiz
            if (customer.getAddress() != null && customer.getAddress().getId() != 0) {
                Address realAddress = addressService.find(customer.getAddress().getId());
                customer.setAddress(realAddress); // Gerçek adresi ayarla
            } else {
                // Eğer body'de adres gelmezse, mevcut adresi koru
                customer.setAddress(existingCustomer.getAddress());
            }

            return customerService.save(customer);
        }
        return null; // Veya exception
    }
    // --- DÜZELTME SONU (PUT) ---

    @DeleteMapping("/{id}")
    public Customer delete(@PathVariable long id) {
        return customerService.delete(id);
    }
}
