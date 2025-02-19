package com.bookstore.appication.usecases;

import com.bookstore.application.dtos.PurchaseDto;
import com.bookstore.application.exceptions.NotFoundException;
import com.bookstore.application.exceptions.ValueErrorException;
import com.bookstore.application.gateways.BookGateway;
import com.bookstore.application.gateways.CustomerGateway;
import com.bookstore.application.models.Book;
import com.bookstore.application.models.BookType;
import com.bookstore.application.models.Customer;
import com.bookstore.application.models.Type;
import com.bookstore.application.usecases.PurchaseUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PurchaseUseCaseTest {

    @Autowired
    PurchaseUseCase purchaseUseCase;

    @MockBean
    CustomerGateway customerGateway;

    @MockBean
    BookGateway bookGateway;


    @Test
    void purchaseSuccessTest() throws NotFoundException, ValueErrorException {
        var customer = Customer.builder().customerId(1L).loyaltyPoints(10L).build();
        var type = Type.builder().typeId(1).name(BookType.REGULAR).build();
        var book = Book.builder().bookId(1L).amount(1L).price(10.0).type(type).build();
        when(customerGateway.getCustomer(1L)).thenReturn(Optional.of(customer));
        when(bookGateway.getBookById(1L)).thenReturn(Optional.of(book));

        var purchaseDto = PurchaseDto.builder().bookId(1L).amount(1L).applyDiscount(false).build();

        var priceDto = purchaseUseCase.purchase(List.of(purchaseDto), 1L);

        assertEquals(10.0, priceDto.getPrice());
        assertEquals(11, customer.getLoyaltyPoints());
        assertEquals(0, book.getAmount());

    }

    @Test
    void purchaseSuccessTestApplyDiscountByBookType() throws NotFoundException, ValueErrorException {
        var customer = Customer.builder().customerId(1L).loyaltyPoints(10L).build();
        var regularType = Type.builder().typeId(1).name(BookType.REGULAR).build();
        var oldType = Type.builder().typeId(1).name(BookType.OLD_EDITIONS).build();
        var regularBook = Book.builder().bookId(1L).amount(2L).price(10.0).type(regularType).build();
        var oldBook = Book.builder().bookId(2L).amount(1L).price(10.0).type(oldType).build();
        when(customerGateway.getCustomer(1L)).thenReturn(Optional.of(customer));
        when(bookGateway.getBookById(1L)).thenReturn(Optional.of(regularBook));
        when(bookGateway.getBookById(2L)).thenReturn(Optional.of(oldBook));

        var purchaseDto = PurchaseDto.builder().bookId(1L).amount(2L).applyDiscount(false).build();
        var purchaseDto2 = PurchaseDto.builder().bookId(2L).amount(1L).applyDiscount(false).build();

        var priceDto = purchaseUseCase.purchase(List.of(purchaseDto, purchaseDto2), 1L);

        assertEquals(25.5, priceDto.getPrice());
        assertEquals(11, customer.getLoyaltyPoints());
        assertEquals(0, regularBook.getAmount());
        assertEquals(0, oldBook.getAmount());

    }

    @Test
    void purchaseSuccessTestApplyDiscountByLoyaltyPoints() throws NotFoundException, ValueErrorException {
        var customer = Customer.builder().customerId(1L).loyaltyPoints(10L).build();
        var type = Type.builder().typeId(1).name(BookType.REGULAR).build();
        var book = Book.builder().bookId(1L).amount(3L).price(10.0).type(type).build();
        when(customerGateway.getCustomer(1L)).thenReturn(Optional.of(customer));
        when(bookGateway.getBookById(1L)).thenReturn(Optional.of(book));

        var purchaseDto = PurchaseDto.builder().bookId(1L).amount(3L).applyDiscount(true).build();

        var priceDto = purchaseUseCase.purchase(List.of(purchaseDto), 1L);

        assertEquals(18.0, priceDto.getPrice());
        assertEquals(0, customer.getLoyaltyPoints());
        assertEquals(0, book.getAmount());
    }

    @Test
    void purchaseSuccessTestThrowsValueErrorException() throws NotFoundException, ValueErrorException {
        var customer = Customer.builder().customerId(1L).loyaltyPoints(10L).build();
        var type = Type.builder().typeId(1).name(BookType.REGULAR).build();
        var book = Book.builder().bookId(1L).amount(3L).price(10.0).type(type).build();
        when(customerGateway.getCustomer(1L)).thenReturn(Optional.of(customer));
        when(bookGateway.getBookById(1L)).thenReturn(Optional.of(book));

        var purchaseDto = PurchaseDto.builder().bookId(1L).amount(4L).applyDiscount(false).build();

        assertThrows(ValueErrorException.class, () -> purchaseUseCase.purchase(List.of(purchaseDto), 1L));
    }

    @Test
    void purchaseSuccessTestThrowsNotFoundException() throws NotFoundException, ValueErrorException {
        when(customerGateway.getCustomer(1L)).thenReturn(Optional.empty());
        var purchaseDto = PurchaseDto.builder().bookId(1L).amount(4L).applyDiscount(false).build();
        assertThrows(NotFoundException.class, () -> purchaseUseCase.purchase(List.of(purchaseDto), 1L));
    }

}
