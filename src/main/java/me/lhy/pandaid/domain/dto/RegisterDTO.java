package me.lhy.pandaid.domain.dto;


import lombok.Data;
import me.lhy.pandaid.util.AccountType;

import java.io.Serial;
import java.io.Serializable;

@Data
public class RegisterDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nickname;

    private String password;

    private DeviceInfoDTO deviceInfo;

    private AccountType accountType = AccountType.USER;
}