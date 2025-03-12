package me.lhy.pandaid.domain.po;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Panda {

    @Schema(description = "表主键")
    @TableField(updateStrategy = FieldStrategy.NEVER)
    private Long id;

    @Schema(description = "熊猫编号")
    private Integer pandaId;

    @Schema(description = "熊猫名")
    private String pandaName;

    @Schema(description = "熊猫年龄")
    private Integer pandaAge;

    @Schema(description = "熊猫性别（雌/雄）")
    private Character pandaSex;

    @Schema(description = "熊猫描述")
    private String pandaInfo;

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
