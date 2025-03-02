package me.lhy.pandaid.domain.dto;


import lombok.Getter;
import lombok.Setter;
import me.lhy.pandaid.util.AccountType;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class RegisterDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nickname;

    private String password;

    private DeviceInfoDTO deviceInfo;

    private AccountType accountType = AccountType.USER;
}