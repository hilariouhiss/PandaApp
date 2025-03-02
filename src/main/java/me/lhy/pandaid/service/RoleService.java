package me.lhy.pandaid.service;

import me.lhy.pandaid.domain.po.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllWithPage(int number,int size);

    Role getOneByName(String name);

    void addOne(Role role);

    void updateOneById(Role role);

    void deleteOneById(Long id);

    void updateOneByName(Role role);

    void deleteOneByName(String name);
}
