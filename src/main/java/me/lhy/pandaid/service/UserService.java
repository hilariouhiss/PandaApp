package me.lhy.pandaid.service;

import me.lhy.pandaid.domain.dto.UserDto;
import me.lhy.pandaid.domain.po.User;

import java.util.List;

public interface UserService {

    // 用户登录
    User login(UserDto userDto);

    // 用户注册
    User register(UserDto userDto);

    // 批量增加用户
    void addUsers(List<UserDto> userDtoList);

    // 删除用户
    void deleteUser(Long userId);

    // 更新用户
    User updateUser(User user);

    // 通过ID查询单个用户
    User getUserById(Long userId);

    // 通过用户名查询单个用户
    User getUserByUsername(String username);

    // 查询所有用户
    List<User> getAllUsers();


}
