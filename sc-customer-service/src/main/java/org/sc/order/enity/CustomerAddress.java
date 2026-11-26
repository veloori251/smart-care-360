package org.sc.order.enity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Table(name = "customer_address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID addressId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_user_id", nullable = false)
    private Customer customer;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "streetLine", nullable = false)
    private String streetLine;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "pincode", nullable = false)
    private String pincode;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;

    public String toFormattedString() {
        return String.format("%s,%s,%s - %s", streetLine, city, state, pincode);
    }
}