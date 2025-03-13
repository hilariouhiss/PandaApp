package me.lhy.pandaid.domain.po;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RolePermission {
    @Schema(description="表主键")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long id;

    @Schema(description="角色ID")
    private Long roleId;

    @Schema(description="权限ID")
    private Long permissionId;

    @Schema(description="创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @Schema(description="更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(description="是否删除")
    @TableLogic
    private Boolean isDeleted;
}
