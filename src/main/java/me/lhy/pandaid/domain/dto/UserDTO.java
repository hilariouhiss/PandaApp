package me.lhy.pandaid.domain.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private String nickname;

    private String phoneNumber;

    private Character gender;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // private List<Role> roles;
}
