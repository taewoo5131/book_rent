package com.book.demo.book.service.impl;

import com.book.demo.account.repository.Account;
import com.book.demo.account.repository.AccountRepository;
import com.book.demo.book.dto.BookDto;
import com.book.demo.book.dto.BookEntrustRequestDto;
import com.book.demo.book.dto.BookRequestDto;
import com.book.demo.book.repository.*;
import com.book.demo.book.service.BookService;
import com.book.demo.common.response.Error;
import com.book.demo.common.response.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookHandler bookHandler;
    private final BookRepository bookRepository;
    private final JdbcBookRepository jdbcBookRepository;
    private final AccountRepository accountRepository;
    private final BookRentRepository bookRentRepository;

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
    public ResponseEntity findAllBook(String pageNo, String sortKind) {
        int page = Integer.parseInt(pageNo) - 1;
        String sql = bookHandler.makeFindBookQuery(page, sortKind);
        List<BookDto> all = jdbcBookRepository.findAll(sql, page);
        return Result.makeResult(HttpStatus.OK, all);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public ResponseEntity rentBook(BookRequestDto requestDto) {
        Long accountId = requestDto.getAccountId();
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null) {
            log.error(Error.BOOK_RENT_FAIL.getMsg());
            return Result.makeError(Error.BOOK_RENT_FAIL);
        }
        List<Long> bookIdList = requestDto.getBookIdList();
        List<Book> allRentBook = bookRepository.findAllRentBook(bookIdList);
        // 대여하려는 찰나에 타인이 대여해감 (PESSIMISTIC_LOCK으로 해당상황 방지)
        if (allRentBook.size() != bookIdList.size()) {
            log.error(Error.BOOK_RENT_FAIL.getMsg());
            return Result.makeError(Error.BOOK_RENT_FAIL);
        }

        for (Book book : allRentBook) {
            book.rentSuccess();
            BookRent bookRent = book.makeBookRent(account);
            bookRentRepository.save(bookRent);
        }
        // 10초 뒤에 자동 반납 처리
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.schedule(() -> bookHandler.returnBookHandler(requestDto),10, TimeUnit.SECONDS);

        return Result.makeResult(HttpStatus.OK, bookIdList);
    }

    @Override
    @Transactional
    public ResponseEntity returnBook(BookRequestDto requestDto) {
        return Result.makeResult(HttpStatus.OK, bookHandler.returnBookHandler(requestDto));
    }
}
