package me.lhy.pandaid.domain.dto;


import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PandaDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Integer pandaId;

    private String pandaName;

    private Integer pandaAge;

    private Character pandaSex;

    private String pandaInfo;
}
