package com.book.demo.book;

import com.book.demo.book.dto.BookDto;
import com.book.demo.book.dto.BookEntrustRequestDto;
import com.book.demo.book.dto.BookRequestDto;
import com.book.demo.book.repository.Book;
import com.book.demo.book.repository.BookRepository;
import com.book.demo.book.service.BookService;
import com.book.demo.common.response.Result;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

    @Test
    @Transactional
    public void 책_리스트_테스트() {
        // given
        String pageNo = "1";
        String sortKind = "0";

        // when
        ResponseEntity allBook = bookService.findAllBook(pageNo, sortKind);
        Result body = (Result) allBook.getBody();
        List<BookDto> book = (List<BookDto>) body.getResult();

        // then
        Assertions.assertThat(book.size()).isGreaterThan(0);
    }

    @Test
    @Transactional
    public void 책_대여_테스트() {
        // given
        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setAccountId(1L);
        List<Long> bookIdList = new ArrayList<>();
        bookIdList.add(31L);
        requestDto.setBookIdList(bookIdList);

        // when
        ResponseEntity response = bookService.rentBook(requestDto);

        // then
        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    @Transactional
    public void 책_반납_테스트() {
        // given
        BookRequestDto requestDto = new BookRequestDto();
        requestDto.setAccountId(13L);
        List<Long> bookIdList = new ArrayList<>();
        bookIdList.add(6L);
        requestDto.setBookIdList(bookIdList);

        // when
        ResponseEntity response = bookService.returnBook(requestDto);

        // then
        Assertions.assertThat(response.getStatusCode().value()).isEqualTo(HttpStatus.OK.value());
    }
}
