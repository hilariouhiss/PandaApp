create database if not exists pandaApp charset utf8mb4 collate utf8mb4_general_ci;

use pandaApp;

create table if not exists users
(
    id           bigint       not null auto_increment comment '用户id',
    username     varchar(255) not null comment '用户名',
    password     varchar(255) not null comment '用户密码',
    phone_number char(11)     not null comment '用户手机号',
    gender       char(1)      not null comment '用户性别（男/女）',
    created_at   datetime default current_timestamp comment '创建时间',
    updated_at   datetime default current_timestamp on update current_timestamp comment '更新时间',
    is_deleted   tinyint  default 0 comment '是否删除',
    primary key pk_users_id (id),
    unique uk_users_username (username),
    unique uk_users_phone_number (phone_number),
    check ( gender IN ('男', '女') )
);

create table if not exists pandas
(
    panda_id   int         not null auto_increment comment '熊猫编号',
    panda_name varchar(50) not null comment '熊猫名',
    panda_age  int comment '熊猫年龄',
    panda_sex  char(1) comment '熊猫性别',
    panda_info varchar(500) comment '熊猫描述',
    created_at datetime default current_timestamp comment '创建时间',
    updated_at datetime default current_timestamp on update current_timestamp comment '更新时间',
    is_deleted tinyint  default 0 comment '是否删除',
    primary key pk_pandas_id (panda_id) comment '主键约束',
    unique uk_pandas_name (panda_name) comment '大熊猫名唯一约束',
    check ( panda_age >= 0 and panda_age <= 100 ),
    check ( panda_sex IN ('雌', '雄') )
);