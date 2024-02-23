package com.book.demo.account.service.impl;

import com.book.demo.account.dto.AccountLoginRequestDto;
import com.book.demo.account.dto.AccountReLoginRequestDto;
import com.book.demo.account.dto.AccountSaveRequestDto;
import com.book.demo.account.repository.Account;
import com.book.demo.account.repository.AccountRepository;
import com.book.demo.account.service.AccountService;
import com.book.demo.common.response.Error;
import com.book.demo.common.response.Result;
import com.book.demo.common.token.JwtToken;
import com.book.demo.common.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public ResponseEntity saveAccount(AccountSaveRequestDto requestDto) {
        if (!validPassword(requestDto.getPassword())) {
            log.error(Error.ACCOUNT_SAVE_FAIL.getMsg());
            return Result.makeError(Error.ACCOUNT_SAVE_FAIL);
        }
        Account account = accountRepository.save(requestDto.toEntity(passwordEncoder.encode(requestDto.getPassword())));
        return Result.makeResult(HttpStatus.OK, account.getId());
    }

    @Override
    @Transactional
    public ResponseEntity loginAccount(AccountLoginRequestDto requestdto) {
        Account account = accountRepository.findByEmail(requestdto.getEmail());
        if (account == null
                || ! passwordEncoder.matches(requestdto.getPassword(), account.getPassword())) {
            log.error(Error.ACCOUNT_LOGIN_FAIL.getMsg());
            return Result.makeError(Error.ACCOUNT_LOGIN_FAIL);
        }
        JwtToken jwtToken = this.makeJwtToken(account);
        return Result.makeResult(HttpStatus.CREATED, jwtToken);
    }

    @Override
    @Transactional
    public ResponseEntity reLoginAccount(AccountReLoginRequestDto requestDto) {
        Long accountId = requestDto.getId();
        String refreshToken = requestDto.getRefreshToken();
        Account account = accountRepository.findById(accountId).orElse(null);
        if (account == null
                || ! Objects.equals(refreshToken, account.getRefreshToken())
                || ! jwtTokenProvider.validToken(refreshToken)) {
            log.error(Error.ACCOUNT_RE_LOGIN_FAIL.getMsg());
            return Result.makeError(Error.ACCOUNT_RE_LOGIN_FAIL);
        }
        JwtToken jwtToken = this.makeJwtToken(account);
        return Result.makeResult(HttpStatus.CREATED, jwtToken);
    }

    private boolean validPassword(String password) {
        String pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,10}$";

        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    private JwtToken makeJwtToken(Account account) {
        String accessToken = jwtTokenProvider.makeAccessJwtToken(account.getId());
        String refreshToken = jwtTokenProvider.makeRefreshJwtToken();
        account.loginSuccess(refreshToken);
        return new JwtToken("Bearer",accessToken,refreshToken);
    }


}
