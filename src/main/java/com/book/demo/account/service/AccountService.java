package com.book.demo.account.service;

import com.book.demo.account.dto.AccountSaveRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {
    public ResponseEntity saveAccount(AccountSaveRequestDto requestDto);
}
