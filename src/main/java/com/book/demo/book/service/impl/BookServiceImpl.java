package com.book.demo.book.service.impl;

import com.book.demo.account.repository.Account;
import com.book.demo.account.repository.AccountRepository;
import com.book.demo.book.dto.BookDto;
import com.book.demo.book.dto.BookEntrustRequestDto;
import com.book.demo.book.repository.Book;
import com.book.demo.book.repository.BookRepository;
import com.book.demo.book.repository.JdbcBookRepository;
import com.book.demo.book.service.BookService;
import com.book.demo.common.response.Error;
import com.book.demo.common.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookHandler bookHandler;
    private final BookRepository bookRepository;
    private final JdbcBookRepository jdbcBookRepository;
    private final AccountRepository accountRepository;

    @Override
    public ResponseEntity entrustBook(BookEntrustRequestDto requestDto) {
        Long accountId = requestDto.getAccountId();
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            log.error(Error.BOOK_ENTRUST_FAIL.getMsg());
            return Result.makeError(Error.BOOK_ENTRUST_FAIL);
        }
        Book book = bookRepository.save(requestDto.toEntity(account));
        return Result.makeResult(HttpStatus.CREATED, new BookDto(book));
    }

    @Override
    public ResponseEntity findAllBook(String pageNo, String sortKind) {
        int page = Integer.parseInt(pageNo) - 1;
        String sql = bookHandler.makeFindBookQuery(page, sortKind);
        List<BookDto> all = jdbcBookRepository.findAll(sql, page);
        return Result.makeResult(HttpStatus.OK, all);
    }
}
