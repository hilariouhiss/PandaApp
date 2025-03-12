package me.lhy.pandaid.util;


import me.lhy.pandaid.domain.dto.DeviceInfoDTO;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UsernameGenerator {

    public static String generate(DeviceInfoDTO deviceInfo) {
        if (deviceInfo == null) {
            throw new IllegalArgumentException("Device info cannot be null");
        }

        // 拼接各个字段
        StringBuilder sb = new StringBuilder();
        if (deviceInfo.getBrand() != null) {
            sb.append(deviceInfo.getBrand());
        }
        if (deviceInfo.getModel() != null) {
            sb.append(deviceInfo.getModel());
        }
        if (deviceInfo.getHardwareIdentifier() != null) {
            sb.append(deviceInfo.getHardwareIdentifier());
        }
        if (deviceInfo.getSystemFeature() != null) {
            sb.append(deviceInfo.getSystemFeature());
        }
        String baseString = sb.toString();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(baseString.getBytes(StandardCharsets.UTF_8));
            String hexHash = bytesToHex(hashBytes);
            // 为了保证用户名不太长，这里仅使用摘要的前8个字符，可根据需要调整长度
            return deviceInfo.getBrand() + "_" + hexHash.substring(0, 8);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    // 辅助方法：将字节数组转换为16进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

}
