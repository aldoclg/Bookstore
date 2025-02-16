package com.bookstore.infrastructure.dataproviders;

import com.bookstore.application.gateways.BookGateway;
import com.bookstore.application.models.Book;
import com.bookstore.infrastructure.dataproviders.repositories.BookRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BookProvider implements BookGateway {

    private BookRepository bookRepository;

    @Override
    public void save(Book book) {
//        log.info("Saving book");
        bookRepository.save(book);
    }

    @Override
    public void saveAll(List<Book> books) {
        bookRepository.saveAll(books);
    }

    @Override
    public List<Book> findAllAvailableBooksPaginated(int page, int size) {
        var pageable = PageRequest.of(page, size);
        return bookRepository.findAllByAmountGreaterThan(0L, pageable);
    }

    @Override
    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }
}
