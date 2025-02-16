package com.bookstore.application.usecases;

import com.bookstore.application.dtos.PriceDto;
import com.bookstore.application.dtos.PurchaseDto;
import com.bookstore.application.exceptions.NotFoundException;
import com.bookstore.application.exceptions.ValueErrorException;
import com.bookstore.application.gateways.BookGateway;
import com.bookstore.application.gateways.CustomerGateway;
import com.bookstore.application.models.Book;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Log4j2
public class PurchaseUseCase {

    private CustomerGateway customerGateway;
    private BookGateway bookGateway;

    private static final Long ZERO_LOYALTY_POINTS = 0L;

    @Transactional(rollbackFor = {ValueErrorException.class, NotFoundException.class})
    public PriceDto purchase(List<PurchaseDto> purchaseDtoList, Long customerId) throws NotFoundException, ValueErrorException {
        double totalPrice = 0L;
        var customerOptional = customerGateway.getCustomer(customerId);
        isEmptyThrowsException(customerOptional, "Customer not found");

        var customer = customerOptional.get();

        var hasBeenAppliedDiscount = false;

        long totalAmount = purchaseDtoList.stream().map(PurchaseDto::getAmount).reduce(0L, Long::sum);

        List<Book> booksToSave = new java.util.ArrayList<>(Collections.emptyList());

        for (var purchaseDto: purchaseDtoList) {
            var bookId = purchaseDto.getBookId();
            var bookOptional = bookGateway.getBookById(bookId);

            isEmptyThrowsException(bookOptional, "Book not found");

            var book = bookOptional.get();
            purchaseAmountBiggerThanBookAmountThrowsException(purchaseDto, book);

            var amount = purchaseDto.getAmount();
            book.setAmount(book.getAmount() - amount);
            booksToSave.add(book);

            var bookType = book.getType().getName();
            if (!hasBeenAppliedDiscount && purchaseDto.isApplyDiscount() && bookType.hasDiscount(customer.getLoyaltyPoints())) {
                log.info("Discount applied");
                hasBeenAppliedDiscount = true;
                continue;
            }

            var bookPrice = book.getPrice();
            totalPrice = totalPrice + (bookPrice * amount * bookType.apply(totalAmount));

        }

        if (hasBeenAppliedDiscount) {
            log.info("Reset loyalty points");
            customer.setLoyaltyPoints(ZERO_LOYALTY_POINTS);
        } else {
            log.info("Increment loyalty points");
            customer.setLoyaltyPoints(customer.getLoyaltyPoints() + 1);
        }

        log.info("Update books");
        bookGateway.saveAll(booksToSave);
        log.info("Update loyalty points");
        customerGateway.save(customer);

        return PriceDto.builder().price(totalPrice).build();
    }

    private void purchaseAmountBiggerThanBookAmountThrowsException(PurchaseDto purchaseDto, Book book) throws ValueErrorException {
        if (purchaseDto.getAmount() > book.getAmount()) {
            log.error("Amount required is bigger than amount available");
            throw new ValueErrorException("Amount required is bigger than amount available");
        }
    }

    private void isEmptyThrowsException(Optional<?> optional, String errorMessage) throws NotFoundException {
        if (optional.isEmpty()) {
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

}
