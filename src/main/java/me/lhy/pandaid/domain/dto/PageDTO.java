package me.lhy.pandaid.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class PageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Min(1)
    private Integer num;

    @NotNull
    private Integer size;
}
