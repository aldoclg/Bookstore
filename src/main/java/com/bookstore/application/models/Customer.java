package com.bookstore.application.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Customers")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "Customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long customerId;

    private Long loyaltyPoints;
}
