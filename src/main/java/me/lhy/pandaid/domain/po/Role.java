package me.lhy.pandaid.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "角色id")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long id;

    @Schema(description = "角色名")
    private String name;

    @Schema(description = "角色描述")
    private String description;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT,
            updateStrategy = FieldStrategy.NEVER)
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(description = "是否删除")
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;

    @TableField(exist = false)
    private List<Permission> permissions;
}
