package com.bookstore.application.usecases;

import com.bookstore.application.dtos.BookDto;
import com.bookstore.application.gateways.BookGateway;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class GetBooksUseCase {

    private BookGateway bookGateway;

    public List<BookDto> getBooks(int page, int size) {
        log.info("Retrieving from page " + page + " with size " + size);
        var books = bookGateway.findAllAvailableBooksPaginated(page, size);
        log.info("Found " + books.size() + " books");
        return books.stream()
                .map(b -> BookDto.builder()
                .bookId(b.getBookId())
                .name(b.getName())
                .price(b.getPrice())
                .type(b.getType().getName())
                .build())
                .toList();
    }
}
