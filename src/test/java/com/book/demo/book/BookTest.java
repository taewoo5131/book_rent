package com.book.demo.book;

import com.book.demo.book.dto.BookEntrustRequestDto;
import com.book.demo.book.repository.Book;
import com.book.demo.book.repository.BookRepository;
import com.book.demo.book.service.BookService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class BookTest {

    @Autowired
    BookService bookService;

    @Autowired
    BookRepository bookRepository;

    @Test
    @Transactional
    public void 책_위탁_테스트() {
        // given
        BookEntrustRequestDto requestDto = new BookEntrustRequestDto();
        requestDto.setName("해리포터");
        requestDto.setPrice(1000);
        requestDto.setIsbn("1231234124-12313");
        requestDto.setAccountId(12L);

        // when
        bookService.entrustBook(requestDto);

        // then
        List<Book> all = bookRepository.findAll();
        Assertions.assertThat(all.size()).isGreaterThan(0);
    }
}
