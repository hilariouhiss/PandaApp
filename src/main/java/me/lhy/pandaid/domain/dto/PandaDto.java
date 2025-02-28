package me.lhy.pandaid.domain.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class PandaDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer pandaId;

    private String pandaName;

    private Integer pandaAge;

    private String pandaSex;

    private Character pandaInfo;
}
