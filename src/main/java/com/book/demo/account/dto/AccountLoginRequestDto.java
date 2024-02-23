package com.book.demo.account.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountLoginRequestDto {
    private String email;
    private String password;
}
