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

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;


    public RoleServiceImpl(RoleMapper roleMapper) {
        this.roleMapper = roleMapper;
    }


    @Override
    public List<Role> getAllWithPage(int pageNum, int pageSize) {
        Page<Role> page = new Page<>(pageNum, pageSize);
        return roleMapper.selectPage(page, null).getRecords();
    }

    @Override
    public Role getOneByID(Long id) {
        return null;
    }

    @Override
    public Role getOneByName(String name) {
        return roleMapper
                .selectOne(new LambdaQueryWrapper<Role>()
                        .eq(Role::getName, name)
                );
    }

    @Override
    public Long getCount() {
        return 0L;
    }

    @Override
    public List<Role> getDeletedWithPage(int pageNum, int pageSize) {
        return List.of();
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

    /**
     * 物理删除已被逻辑删除的权限
     */
    @Override
    public void physicalDelete() {
        // 查询出所有被逻辑删除的角色
        List<Role> roles = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .eq(Role::getDeleted, true));

        // 先删除与权限的关联关系
        // todo
    }
}
