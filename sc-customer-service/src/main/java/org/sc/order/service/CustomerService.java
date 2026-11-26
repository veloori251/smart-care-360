package org.sc.order.service;

import lombok.RequiredArgsConstructor;
import org.sc.order.dto.CustomerProfileResponse;
import org.sc.order.dto.UpdateCustomerRequest;
import org.sc.order.enity.Customer;
import org.sc.order.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public CustomerProfileResponse getMyProfile(UUID userId){
        Customer customer = getOrProvisonCustomer(userId);
        return CustomerProfileResponse.from(customer);
    }

    @Transactional
    public CustomerProfileResponse updateMyProfile(UUID userId, UpdateCustomerRequest request){
        Customer customer = getOrProvisonCustomer(userId);
        customer.setFullName(request.fullName());
        customer.setPhone(request.phone());
        return CustomerProfileResponse.from(customer);
    }

    @Transactional
    private Customer getOrProvisonCustomer(UUID userId) {
        return customerRepository.findById(userId).orElseGet(()-> customerRepository.save(
                Customer.builder()
                        .userId(userId)
                        .active(true)
                        .build()
        ));
    }
}
