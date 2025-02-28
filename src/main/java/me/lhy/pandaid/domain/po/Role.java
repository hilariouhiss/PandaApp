package me.lhy.pandaid.domain.po;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.util.Date;

@Getter
@Setter
public class Role implements GrantedAuthority {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;
    private Boolean deleted;

    @Override
    public String getAuthority() {
        return name;
    }
}
