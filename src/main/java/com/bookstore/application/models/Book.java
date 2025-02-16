package com.bookstore.application.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Books")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "Books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long bookId;

    private String name;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

    private Double price;

    private Long amount;
}
