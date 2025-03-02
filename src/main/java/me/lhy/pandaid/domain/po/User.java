package me.lhy.pandaid.domain.po;


import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class User {

    @Schema(description = "用户表主键")
    @TableId(type = IdType.ASSIGN_ID) // 使用雪花算法生成主键
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long id;

    @Schema(description = "用户唯一标识符")
    private String username;

    @Schema(description = "用户密码")
    private String password;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户手机号")
    private String phoneNumber;

    @Schema(description = "用户性别（男/女）")
    private Character gender;

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
}
