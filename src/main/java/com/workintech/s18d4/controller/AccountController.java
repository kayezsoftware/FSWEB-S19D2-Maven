package com.workintech.s18d4.controller;

import com.workintech.s18d4.entity.Account;
import com.workintech.s18d4.entity.Customer;
import com.workintech.s18d4.service.AccountService;
import com.workintech.s18d4.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/account") // Bütün endpointler /workintech/account altında olacak
public class AccountController {

    private final AccountService accountService;
    private final CustomerService customerService;

    @GetMapping
    public List<Account> findAll() {
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public Account find(@PathVariable long id) {
        return accountService.find(id);
    }

    // README Görev 3: [POST]/workintech/accounts/{customerId}
    @PostMapping("/{customerId}")
    public Account save(@RequestBody Account account, @PathVariable long customerId) {
        Customer customer = customerService.find(customerId);
        if (customer != null) {
            account.setCustomer(customer);
            return accountService.save(account);
        }
        return null; // Veya exception
    }

    // README Görev 3: [PUT]/workintech/accounts/{customerId}
    // ... (findAll, find, save metotları burada)


    @PutMapping("/{customerId}")
    public Account update(@RequestBody Account account, @PathVariable long customerId) {
        Customer customer = customerService.find(customerId);
        if (customer == null) {
            // Müşteri bulunamazsa hata ver
            throw new RuntimeException("Customer with ID " + customerId + " not found.");
        }

        // 1. Gelen 'account' (JSON body) "detached" durumdadır.
        //    Bunu doğrudan 'save' yapamayız.
        // 2. ID'yi (account.getId()) kullanarak 'mevcut' (existing) hesabı DB'den bulmalıyız.
        Account existingAccount = accountService.find(account.getId());

        if (existingAccount == null) {
            // Güncellenecek hesap bulunamazsa hata ver
            throw new RuntimeException("Account with ID " + account.getId() + " not found.");
        }

        // 3. Mevcut hesabın müşterisinin, URL'deki müşteri ID'si ile eşleştiğini kontrol et (Güvenlik/Mantık).
        if (existingAccount.getCustomer() == null || existingAccount.getCustomer().getId() != customerId) {
            throw new RuntimeException("Account ID " + existingAccount.getId() + " does not belong to Customer ID " + customerId);
        }

        // 4. "existingAccount" (DB'den gelen) üzerine, "account" (JSON'dan gelen)
        //    verilerini kopyala.
        existingAccount.setAccountName(account.getAccountName());
        existingAccount.setMoneyAmount(account.getMoneyAmount());
        // 'customer' zaten 'existingAccount' üzerinde doğru ayarlanmış durumda.

        // 5. "existingAccount" (artık "managed" ve güncel) olan nesneyi kaydet.
        return accountService.save(existingAccount);
    }

    @DeleteMapping("/{id}")
    public Account delete(@PathVariable long id) {
        // ... (delete metodu)
        return accountService.delete(id);
    }
}

