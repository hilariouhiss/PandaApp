package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.lhy.pandaid.annotation.LogOperation;
import me.lhy.pandaid.domain.dto.RegisterDto;
import me.lhy.pandaid.domain.dto.UserDto;
import me.lhy.pandaid.domain.po.Role;
import me.lhy.pandaid.domain.po.User;
import me.lhy.pandaid.domain.po.UserRole;
import me.lhy.pandaid.exception.*;
import me.lhy.pandaid.mapper.RoleMapper;
import me.lhy.pandaid.mapper.UserMapper;
import me.lhy.pandaid.mapper.UserRoleMapper;
import me.lhy.pandaid.service.UserService;
import me.lhy.pandaid.util.Constants;
import me.lhy.pandaid.util.Converter;
import me.lhy.pandaid.util.UserIdGenerator;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@CacheConfig(cacheNames = "userSystem")
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserIdGenerator userIdGenerator;


    public UserServiceImpl(UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           RoleMapper roleMapper,
                           UserRoleMapper userRoleMapper, UserIdGenerator userIdGenerator) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.userIdGenerator = userIdGenerator;
    }

    /**
     * 校验注册信息
     * @param dto 注册信息
     */
    private void validateUser(RegisterDto dto) {
        // 基础非空校验
        if (dto.getNickname().isBlank() || dto.getPassword().isBlank() || dto.getPhoneNumber().isBlank()) {
            throw new RegisterInfoEmptyException("注册信息不能为空");
        }
        // 用户昵称校验，长度在1-15位之间
        if(dto.getNickname().length() > 15){
            throw new NicknameTooLongException("用户昵称超过15字符限制");
        }

        // 密码强度校验（至少8位，含大小写字母、数字、特殊字符）
        String passwordPattern = Constants.PASSWORD_PATTERN;
        if (!dto.getPassword().matches(passwordPattern)) {
            throw new PasswordStrengthException("密码需包含大小写字母、数字及特殊字符");
        }

        // 手机号格式校验（至少11位，以数字1开头）
        String phonePattern = Constants.PHONE_NUMBER_PATTERN;
        if (!dto.getPhoneNumber().matches(phonePattern)){
            throw new PhoneFormatException("手机号需为1开头的11位数字");
        }
    }

    /**
     * 注册用户
     *
     * @param dto 注册信息
     */
    @LogOperation(value = "用户注册操作", maskFields = {"password", "phoneNumber"})
    @Transactional(rollbackFor = Exception.class)
    @Override
    @CacheEvict(value = {"user","userCount"}, allEntries = true)
    public void register(RegisterDto dto) {
        // 检查注册信息是否正确
        validateUser(dto);
        // 判断手机号是否已存在
        if (userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhoneNumber, dto.getPhoneNumber())) != null) {
            throw new UserAlreadyExistsException("手机号已存在");
        }
        User user = Converter.INSTANCE.toUser(dto);
        // 生成唯一username(16位英文、数字、下划线和数字点构成)
        String username = userIdGenerator.nextId();
        user.setUsername(username);
        // 加密密码
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        // 插入用户
        userMapper.insert(user);
        // 在user_role关联表中为该用户插入对应的角色
        Role role = roleMapper.selectOne(new LambdaQueryWrapper<Role>().eq(Role::getName, dto.getAccountType()));
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
    @LogOperation(value = "获取所有用户信息")
    @Override
    @Cacheable(value = "users", key = "T(java.util.Objects).hash(#pageNum,#pageSize)",
            condition = "#pageNum < 5")
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
    @LogOperation(value = "获取所有用户信息")
    @Override
    @Cacheable(value = "user", key = "#id")
    public UserDto getOneById(Long id) {
        if (id == null) throw new IllegalArgumentException("id不能为空");
        UserDto dto = Converter.INSTANCE.toUserDto(userMapper.selectById(id));
        if (dto == null) throw new UserNotFoundException("用户不存在");
        return dto;
    }

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @LogOperation(value = "获取所有用户信息")
    @Override
    @Cacheable(value = "user", key = "#username")
    public UserDto getOneByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("username不能为空");
        UserDto dto = Converter.INSTANCE.toUserDto(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username)));
        if (dto == null) throw new UserNotFoundException("用户不存在");
        return dto;
    }

    /**
     * 获取用户总数
     *
     * @return 用户总数
     */
    @LogOperation(value = "获取用户总数")
    @Override
    @Cacheable(value = "userCount")
    public Long getCount() {
        return userMapper.selectCount(null);
    }

    /**
     * 获取已删除的用户
     *
     * @return 已删除的用户
     */
    @LogOperation(value = "获取已删除的用户")
    @Override
    @Cacheable(value = "deletedUsers", key = "#pageNum + '-' + #pageSize")
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
    @CacheEvict(value = {"users", "userCount", "deletedUsers"}, allEntries = true)
    public void addMany(List<UserDto> userDtos) {
        List<User> users = userDtos.stream().map(Converter.INSTANCE::toUser).toList();
        userMapper.insert(users);
    }

    /**
     * 更新单个用户
     *
     * @param userDto 新用户信息
     */
    @LogOperation(value = "更新单个用户")
    @Transactional
    @Override
    @CacheEvict(value = {"user","users"}, allEntries = true)
    public void updateOne(UserDto userDto) {
        userMapper.updateById(Converter.INSTANCE.toUser(userDto));
    }

    /**
     * 根据id删除用户
     *
     * @param id 用户id
     */
    @LogOperation(value = "根据id删除用户")
    @Transactional
    @Override
    @CacheEvict(value = {"user","users","userCount"}, allEntries = true)
    public void deleteOneById(Long id) {
        if (id == null) throw new IllegalArgumentException("id不能为空");
        int deleted = userMapper.deleteById(id);
        if (deleted == 0) throw new UserNotFoundException("用户不存在");
    }

    /**
     * 根据用户名删除用户
     *
     * @param username 用户名
     */
    @LogOperation(value = "根据用户名删除用户")
    @Transactional
    @Override
    @CacheEvict(value = {"user", "users", "userCount"}, allEntries = true)
    public void deleteOneByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("username不能为空");
        int deleted = userMapper.delete(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (deleted == 0) throw new UserNotFoundException("用户不存在");
    }

    /**
     * 批量删除用户
     *
     * @param ids 用户id列表
     */
    @LogOperation(value = "批量删除用户")
    @Transactional
    @Override
    @CacheEvict(value = {"user", "users", "userCount"}, allEntries = true)
    public void deleteMany(List<Long> ids) {
        if (ids == null || ids.isEmpty()) throw new IllegalArgumentException("ids不能为空");
        int deleted = userMapper.deleteByIds(ids);
        if (deleted != ids.size()) throw new BatchProcessException("删除条数不符合预期");
    }
}
