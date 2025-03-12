package me.lhy.pandaid.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer num;

    private Integer size;
}
