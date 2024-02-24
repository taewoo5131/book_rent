package com.book.demo.book.controller;

import com.book.demo.book.dto.BookEntrustRequestDto;
import com.book.demo.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;
    @PostMapping
    public ResponseEntity bookEntrust(@RequestBody @Valid BookEntrustRequestDto requestDto) {
        return bookService.entrustBook(requestDto);
    }

    @GetMapping
    public ResponseEntity bookList(@RequestParam String pageNo, @RequestParam String sortKind) {
        return bookService.findAllBook(pageNo, sortKind);
    }
}
