package com.book.demo.account.controller;

import com.book.demo.account.dto.AccountSaveRequestDto;
import com.book.demo.account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity accountSave(@RequestBody @Valid AccountSaveRequestDto requestDto) {
        return accountService.saveAccount(requestDto);
    }
}
