package org.sc.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.parameters.P;

public record AddressRequest(
        @NotBlank(message = "Label is required (e.g., HOME,WORK)")
        @Size(max = 30)
        String label,
        @NotBlank(message = "Street line is required")
        @Size(max = 255)
        String streetLine,
        @NotBlank(message = "City is required")
        @Size(max = 30)
        String city,
        @NotBlank(message = "State is required")
        @Size(max = 30)
        String state,
        @NotBlank(message = "pincode is required")
        @Pattern(regexp = "^[1-9][0-9]{5}$",message = "Pincode must be a valid 6 digit Indian PIN")
        String pincode,
        boolean isDefault
) {
}
