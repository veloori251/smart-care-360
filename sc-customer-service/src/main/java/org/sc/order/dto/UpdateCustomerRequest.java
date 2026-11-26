package org.sc.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateCustomerRequest(
        @NotBlank(message = "Full name is required")
        @Size(max = 120)
        String fullName,
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+?[0-9\\-\\s]{7,20}$",message = "Invalid phone number format")
        String phone
) {

}
