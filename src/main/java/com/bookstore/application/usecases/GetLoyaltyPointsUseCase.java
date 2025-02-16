package com.bookstore.application.usecases;

import com.bookstore.application.dtos.LoyaltyPointsDto;
import com.bookstore.application.exceptions.NotFoundException;
import com.bookstore.application.gateways.CustomerGateway;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class GetLoyaltyPointsUseCase {

    private CustomerGateway CustomerGateway;

    public LoyaltyPointsDto getLoyaltyPointsByCustomerId(Long customerId) throws NotFoundException {
        var CustomerOptional = CustomerGateway.getCustomer(customerId);
        if (CustomerOptional.isPresent()) {
            return LoyaltyPointsDto.builder()
                    .loyaltyPoints(CustomerOptional.get().getLoyaltyPoints())
                    .build();
        }
        log.error("Customer not found");
        throw new NotFoundException("Customer not found");
    }
}
