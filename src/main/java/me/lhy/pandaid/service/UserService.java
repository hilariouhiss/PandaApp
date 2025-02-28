package me.lhy.pandaid.service;

import me.lhy.pandaid.domain.dto.RegisterDto;
import me.lhy.pandaid.domain.dto.UserDto;

import java.util.List;

public interface UserService{

    // 用户注册
    void register(RegisterDto dto);

    // 查询
    // 查询所有用户
    List<UserDto> getAllWithPage(int pageNum, int pageSize);

    // 根据ID查询单个用户
    UserDto getOneById(Long id);

    // 根据用户名查询单个用户
    UserDto getOneByUsername(String username);

    // 查询所有用户的数量
    Long getCount();

    // 查询被删除的用户
    List<UserDto> getDeletedWithPage(int pageNum, int pageSize);

    // 添加
    // 添加多个用户
    void addMany(List<UserDto> userDtos);

    // 修改
    // 修改单个用户信息
    void updateOne(UserDto userDto);

    // 删除
    // 通过ID删除单个用户
    void deleteOneById(Long id);

    // 通过用户名删除单个用户
    void deleteOneByUsername(String username);

    // 通过ID删除多个用户
    void deleteMany(List<Long> ids);
}
