package com.book.demo.book.repository;

import com.book.demo.book.dto.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class JdbcBookRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcBookRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<BookDto> findAll(String sql,int page) {
        return jdbcTemplate.query(sql,bookDtoRowMapper(),page * 20);
    }

    private RowMapper<BookDto> bookDtoRowMapper() {
        return (rs, rownum) -> {
            BookDto bookDto = new BookDto();
            bookDto.setId(rs.getLong("id"));
            bookDto.setName(rs.getString("name"));
            bookDto.setIsbn(rs.getString("isbn"));
            bookDto.setPrice(rs.getInt("price"));
            bookDto.setAccountId(rs.getLong("account_id"));
            bookDto.setAccountName(rs.getString("account_name"));
            return bookDto;
        };
    }


}
