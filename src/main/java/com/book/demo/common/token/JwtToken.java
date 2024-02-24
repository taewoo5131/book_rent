package com.book.demo.common.token;

import com.book.demo.account.dto.AccountDto;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class JwtToken extends AccountDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
