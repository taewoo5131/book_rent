package com.book.demo.book.dto;

import com.book.demo.account.repository.Account;
import com.book.demo.book.repository.Book;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String name;
    private String isbn;
    private int price;
    private Long accountId;
    private String accountName;

    public BookDto(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.isbn = book.getIsbn();
        this.price = book.getPrice();
        this.accountId = book.getAccount().getId();
        this.accountName = book.getAccount().getName();
    }
}
