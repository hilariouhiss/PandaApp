package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.lhy.pandaid.annotation.LogOperation;
import me.lhy.pandaid.domain.po.Role;
import me.lhy.pandaid.domain.po.UserRole;
import me.lhy.pandaid.mapper.RoleMapper;
import me.lhy.pandaid.mapper.UserRoleMapper;
import me.lhy.pandaid.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    public RoleServiceImpl(RoleMapper roleMapper, UserRoleMapper userRoleMapper) {
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
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
                        .eq(Role::getName, name));
    }

    @Override
    public Long getCount() {
        return roleMapper.selectCount(null);
    }

    @Override
    public List<Role> getDeletedWithPage(int pageNum, int pageSize) {
        var page = new Page<Role>(pageNum, pageSize);
        return roleMapper.selectPage(page, null).getRecords();
    }

    @LogOperation("添加一个角色")
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void addOne(Role role) {
        // 约束检查
        checkName(role.getName());
        checkDescription(role.getDescription());

        roleMapper.insert(role);
    }

    private void checkName(String name){
        if (name.isBlank() || name.length() > 20) {
            throw new RuntimeException("角色名称不符合要求");
        }
    }

    private void checkDescription(String description){
        if (description.isBlank() || description.length() > 50) {
            throw new RuntimeException("角色描述不符合要求");
        }
    }

    @LogOperation("通过ID更新一个角色的信息")
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void updateOneById(Role role) {
        // 字段不为 null 时，进行约束检查
        if (role.getName() != null) {
            checkName(role.getName());
        }
        if (role.getDescription() != null) {
            checkDescription(role.getDescription());
        }
        roleMapper.updateById(role);
    }

    @LogOperation("通过ID删除一个角色")
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void deleteOneById(Long id) {
        // todo 检查是否还有用户拥有此角色
        var wrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getRoleId, id);
        userRoleMapper.selectList(wrapper).clear();
        roleMapper.deleteById(id);
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
