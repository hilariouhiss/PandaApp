package me.lhy.pandaid.service;

import me.lhy.pandaid.domain.po.Permission;

import java.util.List;

public interface PermissionService {

    // 查询
    // 查询所有权限，分页查询
    List<Permission> getAllWithPage(Integer pageNum, Integer pageSize);

    // 根据ID查询单个权限
    Permission getOneById(Long id);

    // 根据URL查询单个权限
    Permission getOneByURL(String url);

    // 根据名称查询单个权限
    Permission getOneByName(String name);

    // 查询所有权限的数量
    Long getCount();

    // 查询被删除的权限
    List<Permission> getDeletedWithPage(int pageNum, int pageSize);

    // 添加
    // 添加单个权限
    void addOne(Permission permission);

    // 添加多个权限
    void addMany(List<Permission> permission);

    // 修改
    // 修改单个权限信息
    void updateOne(Permission permission);

    // 删除
    // 通过ID删除单个权限
    void deleteOneById(Long id);

    // 通过名称删除单个权限
    void deleteOneByName(String name);

    // 通过ID删除多个权限
    void deleteMany(List<Long> ids);

    // 物理删除已逻辑删除的权限
    void physicalDelete();
}
