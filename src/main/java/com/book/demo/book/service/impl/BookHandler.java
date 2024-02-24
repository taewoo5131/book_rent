package com.book.demo.book.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookHandler {

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
}
