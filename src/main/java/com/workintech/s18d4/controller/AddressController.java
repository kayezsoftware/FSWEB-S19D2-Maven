package com.workintech.s18d4.controller;

import com.workintech.s18d4.entity.Address;
import com.workintech.s18d4.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/address") // Bütün endpointler /workintech/address altında olacak
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public List<Address> findAll() {
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    public Address find(@PathVariable long id) {
        return addressService.find(id);
    }

    @PostMapping
    public Address save(@RequestBody Address address) {
        return addressService.save(address);
    }

    @PutMapping("/{id}")
    public Address update(@RequestBody Address address, @PathVariable long id) {
        Address existingAddress = addressService.find(id);
        if (existingAddress != null) {
            address.setId(id);
            return addressService.save(address);
        }
        return null; // Veya exception
    }

    @DeleteMapping("/{id}")
    public Address delete(@PathVariable long id) {
        return addressService.delete(id);
    }
}
