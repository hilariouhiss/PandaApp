package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.lhy.pandaid.annotation.LogOperation;
import me.lhy.pandaid.domain.po.Role;
import me.lhy.pandaid.mapper.RoleMapper;
import me.lhy.pandaid.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("roleService")
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;

    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }


    @Override
    public List<Role> getAllWithPage(int number, int size) {
        Page<Role> page = new Page<>(number, size);
        return roleMapper.selectPage(page, null).getRecords();
    }

    @Override
    public Role getOneByName(String name) {
        return roleMapper
                .selectOne(new LambdaQueryWrapper<Role>()
                        .eq(Role::getName, name)
                );
    }

    @LogOperation("添加一个角色")
    @Transactional
    @Override
    public void addOne(Role role) {
        // todo 检查name和description
        roleMapper.insert(role);
    }

    @LogOperation("通过ID更新一个角色的信息")
    @Transactional
    @Override
    public void updateOneById(Role role) {
        // todo 检查name和description
        roleMapper.updateById(role);
    }

    @LogOperation("通过Name更改一个角色的信息")
    @Transactional
    @Override
    public void updateOneByName(Role role) {
        // todo 检查name和description
        roleMapper.update(role, new LambdaQueryWrapper<Role>()
                .eq(Role::getName, role.getName()));
    }

    @LogOperation("通过ID删除一个角色")
    @Transactional
    @Override
    public void deleteOneById(Long id) {
        // todo 检查是否还有用户拥有此角色
        roleMapper.deleteById(id);
    }

    @LogOperation("通过Name删除一个角色")
    @Transactional
    @Override
    public void deleteOneByName(String name) {
        // todo 检查是否还有用户拥有此角色
        roleMapper.delete(new LambdaQueryWrapper<Role>()
                .eq(Role::getName, name));
    }
}
