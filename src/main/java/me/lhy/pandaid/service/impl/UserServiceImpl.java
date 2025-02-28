package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.lhy.pandaid.annotation.LogOperation;
import me.lhy.pandaid.domain.dto.RegisterDto;
import me.lhy.pandaid.domain.dto.UserDto;
import me.lhy.pandaid.domain.po.Role;
import me.lhy.pandaid.domain.po.SecurityUser;
import me.lhy.pandaid.domain.po.User;
import me.lhy.pandaid.domain.po.UserRole;
import me.lhy.pandaid.exception.UserAlreadyExistsException;
import me.lhy.pandaid.mapper.RoleMapper;
import me.lhy.pandaid.mapper.UserMapper;
import me.lhy.pandaid.mapper.UserRoleMapper;
import me.lhy.pandaid.service.UserService;
import me.lhy.pandaid.util.Converter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;


    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder, RoleMapper roleMapper, UserRoleMapper userRoleMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * 根据用户名加载用户信息
     *
     * @param username 用户名
     * @return 用户信息
     * @throws UsernameNotFoundException 用户不存在
     */
    @LogOperation(
            value = "用户登录操作",
            maskFields = {"password"}
    )
    @Override
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

    /**
     * 验证用户注册信息是否正确
     *
     * @param dto 注册信息
     * @return 信息是否合法
     */
    private boolean validateUser(RegisterDto dto) {
        // 基础非空校验
        if (dto.getUsername().isBlank() || dto.getPassword().isBlank()
                || dto.getPhoneNumber().isBlank()) {
            return false;
        }
        // 密码强度校验（至少8位，含大小写字母、数字、特殊字符）
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,50}$";
        if (!dto.getPassword().matches(passwordPattern)) {
            return false;
        }

        // 手机号格式校验（至少11位，以数字1开头）
        String phonePattern = "^1[3-9]\\d{9}$";
        return dto.getPhoneNumber().matches(phonePattern);
    }

    /**
     * 注册用户
     *
     * @param dto 注册信息
     */
    @LogOperation(
            value = "用户注册操作",
            maskFields = {"password", "phoneNumber"}
    )
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void register(RegisterDto dto) {
        // 检查注册信息是否正确
        if (!validateUser(dto)) {
            throw new IllegalArgumentException("注册信息不合法");
        }
        // 判断用户名是否已存在
        if (userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername())) != null) {
            throw new UserAlreadyExistsException("用户已存在");
        }
        // 判断手机号是否已存在
        if (userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getPhoneNumber, dto.getPhoneNumber())) != null) {
            throw new UserAlreadyExistsException("手机号已存在");
        }
        // 加密密码
        User user = Converter.INSTANCE.toUser(dto);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        // 插入用户
        userMapper.insert(user);
        // 在user_role关联表中为该用户插入对应的角色
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .eq(Role::getName, dto.getAccountType()));
        UserRole userRole = new UserRole(user.getId(), role.getId());
        userRoleMapper.insert(userRole);
    }

    /**
     * 分页查询所有用户
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return 指定页的用户
     */
    @LogOperation(
            value = "获取所有用户信息"
    )
    @Override
    public List<UserDto> getAllWithPage(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        List<User> users = userMapper.selectPage(page, null).getRecords();
        return users.stream().map(Converter.INSTANCE::toUserDto).toList();
    }

    /**
     * 根据id获取用户
     *
     * @param id 用户id
     * @return 用户信息
     */
    @LogOperation(
            value = "获取所有用户信息"
    )
    @Override
    public UserDto getOneById(Long id) {
        return Converter.INSTANCE.toUserDto(userMapper.selectById(id));
    }

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @LogOperation(
            value = "获取所有用户信息"
    )
    @Override
    public UserDto getOneByUsername(String username) {
        return Converter.INSTANCE.toUserDto(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username)));
    }

    /**
     * 获取用户总数
     *
     * @return 用户总数
     */
    @LogOperation(
            value = "获取用户总数"
    )
    @Override
    public Long getCount() {
        return userMapper.selectCount(null);
    }

    /**
     * 获取已删除的用户
     *
     * @return 已删除的用户
     */
    @LogOperation(
            value = "获取已删除的用户"
    )
    @Override
    public List<UserDto> getDeletedWithPage(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        List<User> users = userMapper.selectPage(page, new LambdaQueryWrapper<User>().eq(User::getDeleted, true)).getRecords();
        return users.stream().map(Converter.INSTANCE::toUserDto).toList();
    }

    /**
     * 批量添加用户
     *
     * @param userDtos 多个用户信息
     */
    @LogOperation(value = "批量添加用户")
    @Transactional
    @Override
    public void addMany(List<UserDto> userDtos) {
        List<User> users = userDtos.stream().map(Converter.INSTANCE::toUser).toList();
        userMapper.insert(users);
    }

    /**
     * 更新单个用户
     *
     * @param userDto 新用户信息
     */
    @LogOperation(
            value = "更新单个用户"
    )
    @Transactional
    @Override
    public void updateOne(UserDto userDto) {
        userMapper.updateById(Converter.INSTANCE.toUser(userDto));
    }

    /**
     * 根据id删除用户
     *
     * @param id 用户id
     */
    @LogOperation(
            value = "根据id删除用户"
    )
    @Transactional
    @Override
    public void deleteOneById(Long id) {
        userMapper.deleteById(id);
    }

    /**
     * 根据用户名删除用户
     *
     * @param username 用户名
     */
    @LogOperation(
            value = "根据用户名删除用户"
    )
    @Transactional
    @Override
    public void deleteOneByUsername(String username) {
        userMapper.delete(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户id列表
     */
    @LogOperation(
            value = "批量删除用户"
    )
    @Transactional
    @Override
    public void deleteMany(List<Long> ids) {
        userMapper.deleteByIds(ids);
    }
}
