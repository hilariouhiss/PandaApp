package me.lhy.pandaid.domain.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DeviceInfoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String brand;   // 品牌
    private String model;       // 设备型号
    private String hardwareIdentifier; // 硬件标识
    private String systemFeature;      // 系统特征

}
