package me.lhy.pandaid.annotation;

public @interface LogOperation {

    String value() default ""; // 操作描述

    String[] maskFields() default {};   // 脱敏字段

    boolean trackParams() default true; // 是否记录参数

    boolean trackResult() default false; // 是否记录返回结果

    int maxParamSize() default 3;  // 参数最大记录长度，默认显示3条

    boolean showSample() default true;  // 是否显示样例数据
}
