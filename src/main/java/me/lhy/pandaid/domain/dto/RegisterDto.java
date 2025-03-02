package me.lhy.pandaid.domain.dto;


import lombok.Getter;
import lombok.Setter;
import me.lhy.pandaid.util.AccountType;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class RegisterDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nickname;

    private String password;

    private String phoneNumber;

    private Character gender;

    private LocalDateTime createdAt;

    private AccountType accountType = AccountType.USER;
}