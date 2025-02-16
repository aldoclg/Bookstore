package com.bookstore.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PurchaseDto {
    private Long bookId;
    private Long amount;
    private boolean applyDiscount;
}
