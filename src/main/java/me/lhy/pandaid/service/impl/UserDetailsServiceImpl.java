package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import me.lhy.pandaid.annotation.LogOperation;
import me.lhy.pandaid.domain.po.Role;
import me.lhy.pandaid.domain.po.SecurityUser;
import me.lhy.pandaid.domain.po.User;
import me.lhy.pandaid.mapper.RoleMapper;
import me.lhy.pandaid.mapper.UserMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@CacheConfig(cacheNames = "userSystem")
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;

    public UserDetailsServiceImpl(UserMapper userMapper, RoleMapper roleMapper) {
        this.userMapper = userMapper;
        this.roleMapper = roleMapper;
    }


    /**
     * 根据用户名加载用户信息
     *
     * @param username 用户名
     * @return 用户信息
     * @throws UsernameNotFoundException 用户不存在
     */
    @LogOperation(value = "用户登录操作", maskFields = {"password"})
    @Override
    @Cacheable(value = "user", key = "#username", unless = "#result == null")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var wrapper = new LambdaQueryWrapper<User>().eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        // 获取用户所有角色
        List<Role> roles = roleMapper.getUserRoles(user.getId());
        // 返回包含用户名，密码和权限的用户实体
        return new SecurityUser(user, roles);
    }
}
