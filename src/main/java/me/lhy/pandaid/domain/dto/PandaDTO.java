package me.lhy.pandaid.domain.dto;


import jakarta.validation.constraints.*;
import lombok.Data;
import me.lhy.pandaid.Insert;
import me.lhy.pandaid.Update;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PandaDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Null(groups = Insert.class, message = "主键必须为空")
    @NotNull(groups = Update.class, message = "主键不可为空")
    private Long id;

    @NotNull(groups = Insert.class, message = "大熊猫编号不可为空")
    private Integer pandaId;

    @NotBlank(groups = Insert.class, message = "大熊猫名称不可为空")
    @Size(min = 1, max = 50, message = "大熊猫名称长度应在1-50")
    private String pandaName;

    @NotNull(groups = {Insert.class}, message = "大熊猫年龄不可为空")
    @Min(groups = {Insert.class, Update.class}, value = 0, message = "大熊猫年龄不可为负")
    private Integer pandaAge;

    @Pattern(
            groups = Insert.class,
            regexp = "^[雌雄]$",
            message = "大熊猫性别只能为 雌/雄"
    )
    private Character pandaSex;

    @NotBlank(groups = Insert.class, message = "大熊猫描述不可为空")
    @Size(groups = {Insert.class}, max = 500, message = "大熊猫描述长度应在0-500")
    private String pandaInfo;
}
