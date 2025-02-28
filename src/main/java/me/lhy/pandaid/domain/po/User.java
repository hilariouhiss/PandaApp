package me.lhy.pandaid.domain.po;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class User {

    @Schema(description="用户id")
    private Long id;

    @Schema(description="用户唯一ID")
    private String username;

    @Schema(description="用户密码")
    private String password;

    @Schema(description="用户昵称")
    private String nickname;

    @Schema(description="用户手机号")
    private String phoneNumber;

    @Schema(description="用户性别（男/女）")
    private Character gender;

    @Schema(description="创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    @Schema(description="更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

    @Schema(description="是否删除")
    @TableLogic
    private Boolean deleted;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender='" + gender + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                '}';
    }
}
