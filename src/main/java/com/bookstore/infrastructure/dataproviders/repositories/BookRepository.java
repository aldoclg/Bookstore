package com.bookstore.infrastructure.dataproviders.repositories;


import com.bookstore.application.models.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    List<Book> findAllByAmountGreaterThan(Long amount, Pageable pageable);
}
