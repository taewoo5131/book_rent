package com.book.demo.book.service.impl;

import com.book.demo.book.dto.BookRequestDto;
import com.book.demo.book.repository.BookRent;
import com.book.demo.book.repository.BookRentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookHandler {
    private final BookRentRepository bookRentRepository;

    private final String RENT_DESC = "0";
    private final String PRICE_ASC = "1";
    private final String DATE_DESC = "2";


    public String makeFindBookQuery(int pageNo, String sortKind) {
        StringBuilder sb = new StringBuilder();
        sb.append("""
                SELECT 
                    b.id
                    ,b.name
                    ,b.isbn
                    ,b.price
                    ,a.id AS account_id
                    ,a.name AS account_name
                    ,b.created_date
                    ,b.rent_cnt
                FROM book b
                JOIN account a
                ON b.account_id = a.id
                WHERE 1=1
                AND b.id NOT IN (
                    SELECT br.book_id
                    FROM book_rent br
                    WHERE 1=1
                    AND br.return_flag = 0
                ) 
                """);
        if (sortKind.equals(RENT_DESC)) {
            sb.append("ORDER BY b.rent_cnt DESC");
        } else if (sortKind.equals(PRICE_ASC)) {
            sb.append("ORDER BY b.price ASC");
        } else if (sortKind.equals(DATE_DESC)) {
            sb.append("ORDER BY b.created_date DESC");
        }
        sb.append(" LIMIT ? , 20");
        log.info(sb.toString());
        return sb.toString();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<Long> returnBookHandler(BookRequestDto requestDto) {
        Long accountId = requestDto.getAccountId();
        List<Long> bookIdList = requestDto.getBookIdList();
        for (Long bookId : bookIdList) {
            BookRent bookRent = bookRentRepository.findByAccountIdAndBookIdAndReturnFlag(accountId, bookId, false);
            bookRent.returnBook();
        }
        return bookIdList;
    }
}
