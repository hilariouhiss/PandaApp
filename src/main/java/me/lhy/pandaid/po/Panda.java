package me.lhy.pandaid.po;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@TableName("pandas")
public class Panda {

    @Schema(description="熊猫编号")
    @TableId
    private Integer pandaId;

    @Schema(description="熊猫名")
    private String pandaName;

    @Schema(description="熊猫年龄")
    private Integer pandaAge;

    @Schema(description="熊猫性别（雌/雄）")
    private String pandaSex;

    @Schema(description="熊猫描述")
    private String pandaInfo;

    @Schema(description="创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createdAt;

    @Schema(description="更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updatedAt;

    @Schema(description="是否删除")
    @TableLogic
    private Boolean deleted;
}
