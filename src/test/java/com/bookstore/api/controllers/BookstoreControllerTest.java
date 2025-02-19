package com.bookstore.api.controllers;


import com.bookstore.application.dtos.BookDto;
import com.bookstore.application.dtos.LoyaltyPointsDto;
import com.bookstore.application.dtos.PriceDto;
import com.bookstore.application.dtos.PurchaseDto;
import com.bookstore.application.exceptions.NotFoundException;
import com.bookstore.application.models.BookType;
import com.bookstore.application.usecases.GetBooksUseCase;
import com.bookstore.application.usecases.GetLoyaltyPointsUseCase;
import com.bookstore.application.usecases.PurchaseUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookstoreController.class)
public class BookstoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GetBooksUseCase getBooksUseCase;

    @MockBean
    private GetLoyaltyPointsUseCase getLoyaltyPointsUseCase;

    @MockBean
    private PurchaseUseCase purchaseUseCase;

    @Test
    void returnsBooksAvailableTest() throws Exception {
        var response = List.of(BookDto.builder().type(BookType.NEW_RELEASES).bookId(1L).name("Test").price(2.0).build());
        when(getBooksUseCase.getBooks(0, 100)).thenReturn(response);
        this.mockMvc.perform(get("/v1/books"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"data\":[{\"book_id\":1,\"name\":\"Test\",\"price\":2.0,\"type\":\"NEW_RELEASES\"}]}")));
    }

    @Test
    void returnsLoyaltyPointsTest() throws Exception {
        var response = LoyaltyPointsDto.builder().loyaltyPoints(10L).build();
        when(getLoyaltyPointsUseCase.getLoyaltyPointsByCustomerId(1L)).thenReturn(response);
        this.mockMvc.perform(get("/v1/customers/1/loyalty-points"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"data\":{\"loyalty_points\":10}}")));
    }

    @Test
    void returnsLoyaltyPointsNotFoundTest() throws Exception {
        when(getLoyaltyPointsUseCase.getLoyaltyPointsByCustomerId(1L)).thenThrow(NotFoundException.class);
        this.mockMvc.perform(get("/v1/customers/1/loyalty-points"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void purchaseAndReturnsPriceTest() throws Exception {
        var request = PurchaseDto.builder().bookId(1L).amount(1L).applyDiscount(false).build();
        when(purchaseUseCase.purchase(List.of(request), 1L)).thenReturn(PriceDto.builder().price(10.0).build());
        this.mockMvc.perform(post("/v1/customers/1/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                        "\t\"data\": [\n" +
                        "\t\t{\n" +
                        "\t\t\t\"book_id\": 1,\n" +
                        "\t\t\t\"amount\": 1,\n" +
                        "\t\t\t\"apply_discount\": false\n" +
                        "\t\t}]}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("{\"data\":{\"price\":10.0}}")));
    }

    @Test
    void notPurchaseAndReturnsUnprocessableEntityTest() throws Exception {
        var request = PurchaseDto.builder().bookId(1L).amount(1L).applyDiscount(false).build();
        when(purchaseUseCase.purchase(List.of(request), 1L)).thenThrow(NotFoundException.class);
        this.mockMvc.perform(post("/v1/customers/1/purchases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "\t\"data\": [\n" +
                                "\t\t{\n" +
                                "\t\t\t\"book_id\": 1,\n" +
                                "\t\t\t\"amount\": 1,\n" +
                                "\t\t\t\"apply_discount\": false\n" +
                                "\t\t}]}"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }
}
