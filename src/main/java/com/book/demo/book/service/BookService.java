package com.book.demo.book.service;

import com.book.demo.book.dto.BookEntrustRequestDto;
import org.springframework.http.ResponseEntity;

public interface BookService {
    ResponseEntity entrustBook(BookEntrustRequestDto requestDto);

    ResponseEntity findAllBook(String pageNo, String sortKind);
}
