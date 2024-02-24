package com.book.demo.book.repository;

import com.book.demo.account.repository.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@DynamicInsert
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String isbn;
    private int price;
    private Date createdDate;
    private int rentCnt;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public void rentSuccess() {
        this.rentCnt++;
    }

    public BookRent makeBookRent(Account rentAccount) {
        return BookRent.builder()
                .book(this)
                .account(rentAccount)
                .returnFlag(false)
                .build();
    }
}
