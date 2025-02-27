package me.lhy.pandaid.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class PageDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer num;

    private Integer size;

    @Override
    public String toString() {
        return "PageDto{" +
                "pageNum=" + num +
                ", pageSize=" + size +
                '}';
    }
}
