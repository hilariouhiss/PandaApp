package me.lhy.pandaid.service;

import me.lhy.pandaid.domain.po.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllWithPage(int pageNum,int pageSize);

    Role getOneByID(Long id);

    Role getOneByName(String name);

    Long getCount();

    List<Role> getDeletedWithPage(int pageNum, int pageSize);

    void addOne(Role role);

    void updateOneById(Role role);

    void deleteOneById(Long id);

    void physicalDelete();
}
