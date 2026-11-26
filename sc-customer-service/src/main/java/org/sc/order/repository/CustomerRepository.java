package org.sc.order.repository;

import org.sc.order.enity.Customer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository  extends JpaRepository<Customer,UUID> {

    @EntityGraph(attributePaths = {"address"})
    Optional<Customer> findWithAddressesByUserId(UUID userId);
}
