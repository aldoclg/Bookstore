package com.bookstore.application.gateways;

import com.bookstore.application.models.Book;

import java.util.List;
import java.util.Optional;

public interface BookGateway {

    void save(Book book);
    void saveAll(List<Book> books);
    List<Book> findAllAvailableBooksPaginated(int page, int size);
    Optional<Book> getBookById(Long id);
}
