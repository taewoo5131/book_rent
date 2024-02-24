package com.book.demo.book.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(nativeQuery = true,
        value =
        """
        SELECT 
            b.id
            ,b.name
            ,b.isbn
            ,b.price
            ,a.id AS account_id
            ,b.created_date
            ,b.rent_cnt
        FROM book b
        JOIN account a
        ON b.account_id = a.id
        WHERE 1=1
        AND b.id IN :bookIdList
        AND b.id NOT IN (
            SELECT book_id
            FROM book_rent br
            WHERE 1=1
            AND br.return_flag = 0
            AND br.book_id in :bookIdList 
        )
        FOR UPDATE
    """)
    List<Book> findAllRentBook(List<Long> bookIdList);
}
