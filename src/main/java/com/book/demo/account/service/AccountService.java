package com.book.demo.account.service;

import com.book.demo.account.dto.AccountLoginRequestDto;
import com.book.demo.account.dto.AccountReLoginRequestDto;
import com.book.demo.account.dto.AccountSaveRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AccountService {
    ResponseEntity saveAccount(AccountSaveRequestDto requestDto);

    ResponseEntity loginAccount(AccountLoginRequestDto requestdto);

    ResponseEntity reLoginAccount(AccountReLoginRequestDto requestDto);
}
