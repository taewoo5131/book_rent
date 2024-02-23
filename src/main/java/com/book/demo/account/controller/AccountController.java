package com.book.demo.account.controller;

import com.book.demo.account.dto.AccountLoginRequestDto;
import com.book.demo.account.dto.AccountReLoginRequestDto;
import com.book.demo.account.dto.AccountSaveRequestDto;
import com.book.demo.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity accountSave(@RequestBody @Valid AccountSaveRequestDto requestDto) {
        return accountService.saveAccount(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity accountLogin(@RequestBody AccountLoginRequestDto requestdto) {
        return accountService.loginAccount(requestdto);
    }

    @PostMapping("/re-login")
    public ResponseEntity accountReLogin(@RequestBody AccountReLoginRequestDto requestDto) {
        return accountService.reLoginAccount(requestDto);
    }
}
