package me.lhy.pandaid.domain.po;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class Permission {
    @Schema(description = "权限主键")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long id;

    @Schema(description = "权限名称")
    private String name;

    @Schema(description = "权限对应的URL")
    private String url;

    @Schema(description = "权限功能描述")
    private String description;

    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

    @Schema(description = "是否被删除")
    @TableLogic
    private Integer isDeleted;
}
