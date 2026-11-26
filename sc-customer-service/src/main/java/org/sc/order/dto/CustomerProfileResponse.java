package org.sc.order.dto;

import org.sc.order.enity.Customer;

import java.util.List;
import java.util.UUID;

public record CustomerProfileResponse(
        UUID userId,
        String fullName,
        String phone,
        boolean active,
        List<AddressResponse> addresses
) {
    public static CustomerProfileResponse from(Customer customer){
        List<AddressResponse> addressList = customer.getAddresses().stream()
                .map(AddressResponse::from)
                .toList();
        return new CustomerProfileResponse(
                customer.getUserId(),
                customer.getFullName(),
                customer.getPhone(),
                customer.isActive(),
                addressList
        );
    }
}
