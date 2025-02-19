package com.bookstore.application.dtos;

import com.bookstore.application.models.BookType;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private Long bookId;
    private String name;
    private Double price;
    private BookType type;
}
