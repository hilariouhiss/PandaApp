package me.lhy.pandaid.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serial;
import java.time.LocalDateTime;

@Getter
@Setter
public class Role implements GrantedAuthority {

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

    @Override
    public String getAuthority() {
        return this.name;
    }
}
