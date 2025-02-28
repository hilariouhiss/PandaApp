package me.lhy.pandaid.domain.dto;


import lombok.Getter;
import lombok.Setter;
import me.lhy.pandaid.util.AccountType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class RegisterDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    private String phoneNumber;

    private Character gender;

    private Date createdAt;

    private AccountType accountType = AccountType.USER;
}