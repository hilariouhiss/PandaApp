package me.lhy.pandaid.domain.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.lhy.pandaid.domain.po.Role;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private String nickname;

    private String phoneNumber;

    private Character gender;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<Role> roles;

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", roles=" + roles +
                '}';
    }
}
