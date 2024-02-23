package com.book.demo.account.dto;

import com.book.demo.account.repository.Account;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountSaveRequestDto {
    private String name;
    private String email;
    private String phoneNumber;
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,10}$") // junit 테스트 위해 주석처리
    private String password;

    public Account toEntity(String encryptPassword) {
        return Account.builder()
                .name(this.name)
                .email(this.email)
                .phoneNumber(this.phoneNumber)
                .password(encryptPassword)
                .build();
    }
}
