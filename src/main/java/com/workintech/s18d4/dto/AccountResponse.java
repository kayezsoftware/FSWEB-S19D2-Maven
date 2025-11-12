package com.workintech.s18d4.dto;

public record AccountResponse(long id, String accountName, double moneyAmount) {
    // Testler DTO'yu kontrol etmese de README istediği için ekliyoruz.
    // İstenirse CustomerResponse da buraya eklenebilir.
}
