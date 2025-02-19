package com.bookstore.appication.usecases;

import com.bookstore.application.dtos.BookDto;
import com.bookstore.application.gateways.BookGateway;
import com.bookstore.application.models.Book;
import com.bookstore.application.models.BookType;
import com.bookstore.application.models.Type;
import com.bookstore.application.usecases.GetBooksUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GetBooksUseCaseTest {

    @Autowired
    GetBooksUseCase getBooksUseCase;

    @MockBean
    private BookGateway bookGatewayMock;

    @Test
    void getBooksTest() {
        var type = Type.builder().typeId(1).name(BookType.REGULAR).build();
        var book = Book.builder().bookId(1L).name("Book 1").type(type).price(10.0).amount(10L).build();
        when(bookGatewayMock.findAllAvailableBooksPaginated(0, 100)).thenReturn(List.of(book));
        var result = getBooksUseCase.getBooks(0, 100);
        var expected = BookDto.builder().bookId(1L).price(10.0).name("Book 1").type(BookType.REGULAR).build();
        assertThat(result, is(List.of(expected)));
    }

}
