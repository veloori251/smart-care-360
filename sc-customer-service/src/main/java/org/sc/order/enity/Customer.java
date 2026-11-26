package org.sc.order.enity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {

    @Id
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "full_name", length = 120)
    private String fullName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "active", nullable = false)
    @Builder.Default
    private boolean active =  true;

    @OneToMany(mappedBy = "customer",cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CustomerAddress> addresses = new ArrayList<>();

    @Column(name = "created_at",nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at",nullable = false)
    private Instant updatedAt;

    public void addAddress(CustomerAddress address) {
        addresses.add(address);
        address.setCustomer(this);
    }

    public void removeAddress(CustomerAddress address){
        addresses.remove(address);
        address.setCustomer(null);
    }

    @PrePersist
    void onCreate(){
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate(){
        this.updatedAt = Instant.now();
    }
}
