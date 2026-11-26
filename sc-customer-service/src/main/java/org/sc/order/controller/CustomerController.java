package org.sc.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sc.order.dto.CustomerProfileResponse;
import org.sc.order.dto.UpdateCustomerRequest;
import org.sc.order.service.CustomerService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/me")
    public CustomerProfileResponse getMyProfile(Authentication authentication){
        UUID userId = UUID.fromString(authentication.getName());
        return customerService.getMyProfile(userId);
    }

    @PutMapping("/me")
    public CustomerProfileResponse updateMyProfile(Authentication authentication,
                                                   @Valid @RequestBody UpdateCustomerRequest request){

        UUID userId = UUID.fromString(authentication.getName());
        return customerService.updateMyProfile(userId,request);
    }
}
