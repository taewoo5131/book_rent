package com.book.demo.account.service.impl;

import com.book.demo.account.dto.AccountSaveRequestDto;
import com.book.demo.account.repository.Account;
import com.book.demo.account.repository.AccountRepository;
import com.book.demo.account.service.AccountService;
import com.book.demo.common.response.Error;
import com.book.demo.common.response.Result;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity saveAccount(AccountSaveRequestDto requestDto) {
        if (!validPassword(requestDto.getPassword())) {
            return Result.makeError(Error.ACCOUNT_SAVE_FAIL);
        }
        Account account = accountRepository.save(requestDto.toEntity(passwordEncoder.encode(requestDto.getPassword())));
        return Result.makeResult(HttpStatus.OK, account.getId());
    }

    private boolean validPassword(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,10}$";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(password);
        return m.matches();
    }
}
