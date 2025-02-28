package me.lhy.pandaid.domain.po;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRole {

    @Schema(description="主键id")
    private Long id;

    @Schema(description="用户id")
    private Long userId;

    @Schema(description="角色id")
    private Long roleId;

    public UserRole(Long userId, Long roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
