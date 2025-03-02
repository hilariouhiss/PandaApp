package me.lhy.pandaid.domain.po;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Panda {

    @Schema(description="熊猫编号")
    @TableId
    private Integer pandaId;

    @Schema(description="熊猫名")
    private String pandaName;

    @Schema(description="熊猫年龄")
    private Integer pandaAge;

    @Schema(description="熊猫性别（雌/雄）")
    private Character pandaSex;

    @Schema(description="熊猫描述")
    private String pandaInfo;

    @Schema(description="创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @Schema(description="更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @Schema(description="是否删除")
    @TableLogic
    @TableField("is_deleted")
    private Boolean deleted;

    @Override
    public String toString() {
        return "Panda{" +
                "pandaId=" + pandaId +
                ", pandaName='" + pandaName + '\'' +
                ", pandaAge=" + pandaAge +
                ", pandaSex='" + pandaSex + '\'' +
                ", pandaInfo='" + pandaInfo + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", deleted=" + deleted +
                '}';
    }
}
