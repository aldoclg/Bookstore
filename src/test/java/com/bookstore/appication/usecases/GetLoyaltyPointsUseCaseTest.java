package com.bookstore.appication.usecases;

import com.bookstore.application.dtos.LoyaltyPointsDto;
import com.bookstore.application.exceptions.NotFoundException;
import com.bookstore.application.gateways.CustomerGateway;
import com.bookstore.application.models.Customer;
import com.bookstore.application.usecases.GetLoyaltyPointsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class GetLoyaltyPointsUseCaseTest {

    @Autowired
    GetLoyaltyPointsUseCase getLoyaltyPointsUseCase;

    @MockBean
    CustomerGateway customerGateway;

    @Test
    void getLoyaltyPointsByCustomerId() throws NotFoundException {
        var customer = Customer.builder().customerId(1l).loyaltyPoints(10L).build();
        var expected = LoyaltyPointsDto.builder().loyaltyPoints(10L).build();
        when(customerGateway.getCustomer(1L)).thenReturn(Optional.of(customer));
        var result = getLoyaltyPointsUseCase.getLoyaltyPointsByCustomerId(1L);

        assertEquals(expected, result);
    }

    @Test
    void getLoyaltyPointsByCustomerIdNotFound() throws NotFoundException {
        when(customerGateway.getCustomer(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> getLoyaltyPointsUseCase.getLoyaltyPointsByCustomerId(1L));
    }


}
