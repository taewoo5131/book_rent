package com.book.demo.account.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountReLoginRequestDto {
    private Long id;
    private String refreshToken;
}
