package me.lhy.pandaid.util;

public class Constants {

    // mybatis-plus 批量插入大小
    public static final int INSERT_BATCH_SIZE = 1000;
    // jwt 签发者
    public static final String JWT_ISSUER = "panda-app";
    // security 暴露的 header
    public static final String EXPOSURE_HEADER = "Token";
    // jwt 密钥
    public static final String JWT_SECRET_KEY = "5oiR55qEQmFzZTY05a+G6ZKl";
    // 密码强度校验正则
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&*()_+!]).{8,}$";
    // 电话号码正则
    public static final String PHONE_NUMBER_PATTERN = "^1[3-9]\\d{9}$";

}
