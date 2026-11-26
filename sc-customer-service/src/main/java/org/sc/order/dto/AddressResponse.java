package org.sc.order.dto;

import org.sc.order.enity.CustomerAddress;

import java.util.UUID;

public record AddressResponse(
        UUID addressId,
        String label,
        String streetLine,
        String city,
        String state,
        String pincode,
        boolean isDefault,
        String formattedString
) {

    public static AddressResponse from(CustomerAddress address){
        return new AddressResponse(
                address.getAddressId(),
                address.getLabel(),
                address.getStreetLine(),
                address.getCity(),
                address.getState(),
                address.getPincode(),
                address.isDefault(),
                address.toFormattedString()
        );
    }
}
