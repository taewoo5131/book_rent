package com.book.demo.book.dto;

import com.book.demo.account.repository.Account;
import com.book.demo.book.repository.Book;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class BookEntrustRequestDto extends BookDto{
    public Book toEntity(Account account) {
        return Book.builder()
                .name(super.getName())
                .isbn(super.getIsbn())
                .price(super.getPrice())
                .account(account)
                .build();
    }
}
