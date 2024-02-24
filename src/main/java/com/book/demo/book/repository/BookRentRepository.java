package com.book.demo.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRentRepository  extends JpaRepository<BookRent, Long> {
    BookRent findByAccountIdAndBookIdAndReturnFlag(Long accountId, Long BookId, boolean returnFlag);
}
