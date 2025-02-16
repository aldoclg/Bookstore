package com.bookstore.api.controllers;


import com.bookstore.api.dtos.DataTemplateJson;
import com.bookstore.application.dtos.BookDto;
import com.bookstore.application.dtos.LoyaltyPointsDto;
import com.bookstore.application.dtos.PriceDto;
import com.bookstore.application.dtos.PurchaseDto;
import com.bookstore.application.exceptions.NotFoundException;
import com.bookstore.application.exceptions.ValueErrorException;
import com.bookstore.application.usecases.GetBooksUseCase;
import com.bookstore.application.usecases.GetLoyaltyPointsUseCase;
import com.bookstore.application.usecases.PurchaseUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class BookstoreController {

    private GetBooksUseCase getBooksUseCase;
    private GetLoyaltyPointsUseCase getLoyaltyPointsUseCase;
    private PurchaseUseCase purchaseUseCase;

    @GetMapping("/books")
    public ResponseEntity<DataTemplateJson<List<BookDto>>> getBooks(@RequestParam(name = "page", defaultValue = "0", required = false) int page,
                                                                    @RequestParam(name = "size", defaultValue = "100", required = false) int size) {
        var books = getBooksUseCase.getBooks(page, size);
        var body = new DataTemplateJson<>(books);
        return ResponseEntity.ok(body);
    }

    @GetMapping("/customers/{customerId}/loyalty-points")
    public ResponseEntity<DataTemplateJson<LoyaltyPointsDto>> getLoyaltyPoints(@PathVariable(value = "customerId", required = true) Long CustomerId) {
        try {
            var loyaltyPointsDto = getLoyaltyPointsUseCase.getLoyaltyPointsByCustomerId(CustomerId);
            var body = new DataTemplateJson<>(loyaltyPointsDto);
            return ResponseEntity.ok(body);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping("/customers/{customerId}/purchases")
    public ResponseEntity<DataTemplateJson<PriceDto>> purchase(@PathVariable(value = "customerId", required = true) Long customerId,
                                                               @RequestBody DataTemplateJson<List<PurchaseDto>> requestBody) {

        try {
            var purchaseDtos = requestBody.getData();
            var pricedto = purchaseUseCase.purchase(purchaseDtos, customerId);
            return ResponseEntity.ok(new DataTemplateJson<>(pricedto));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (ValueErrorException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }
}
