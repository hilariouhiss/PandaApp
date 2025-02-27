package me.lhy.pandaid.domain.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;

    private String phoneNumber;

    private Character gender;

    private Date createdAt;

    private Date updatedAt;

    @Override
    public String toString() {
        return "UserDto{" +
                "username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
