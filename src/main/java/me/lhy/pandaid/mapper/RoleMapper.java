package me.lhy.pandaid.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.lhy.pandaid.domain.po.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    // 根据用户id获取用户拥有的角色
    List<Role> getUserRoles(Long id);
}
