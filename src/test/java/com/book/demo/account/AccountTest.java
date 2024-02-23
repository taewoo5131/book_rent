package com.book.demo.account;

import com.book.demo.account.dto.AccountSaveRequestDto;
import com.book.demo.account.repository.Account;
import com.book.demo.account.repository.AccountRepository;
import com.book.demo.account.service.AccountService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
public class AccountTest {

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Test
    @Transactional
    public void 회원저장() {
        // given
        AccountSaveRequestDto requestDto = new AccountSaveRequestDto("admin2","ltw@gmail.com","01023445131","abcd");

        // when
        ResponseEntity response = accountService.saveAccount(requestDto);

        // then
        List<Account> all = accountRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);
    }
}
