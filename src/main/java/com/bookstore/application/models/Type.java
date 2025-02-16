package com.bookstore.application.models;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "Types")
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "Types")
public class Type {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer typeId;

    @Enumerated(EnumType.STRING)
    private BookType name;
}
