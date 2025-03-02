package me.lhy.pandaid.service;

import me.lhy.pandaid.domain.dto.RegisterDTO;
import me.lhy.pandaid.domain.dto.UserDTO;

import java.util.List;

public interface UserService {

    // 用户注册
    void register(RegisterDTO dto);

    // 查询
    // 查询所有用户
    List<UserDTO> getAllWithPage(int pageNum, int pageSize);

    // 根据ID查询单个用户
    UserDTO getOneById(Long id);

    // 根据用户名查询单个用户
    UserDTO getOneByUsername(String username);

    // 查询所有用户的数量
    Long getCount();

    // 查询被删除的用户
    List<UserDTO> getDeletedWithPage(int pageNum, int pageSize);

    // 添加
    // 添加多个用户
    void addMany(List<UserDTO> userDTOS);

    // 修改
    // 修改单个用户信息
    void updateOne(UserDTO userDto);

    // 删除
    // 通过用户名删除单个用户
    void deleteOneByUsername(String username);
}
