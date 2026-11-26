package org.sc.order.dto;

import org.sc.order.enity.Customer;

import java.util.UUID;

public record CustomerProfileResponse(
        UUID userId,
        String fullName,
        String phone,
        boolean active
) {
    public static CustomerProfileResponse from(Customer customer){
        return new CustomerProfileResponse(
                customer.getUserId(),
                customer.getFullName(),
                customer.getPhone(),
                customer.isActive()
        );
    }
}
