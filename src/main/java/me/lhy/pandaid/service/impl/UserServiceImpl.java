package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.lhy.pandaid.annotation.LogOperation;
import me.lhy.pandaid.domain.dto.RegisterDTO;
import me.lhy.pandaid.domain.dto.UserDTO;
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
import me.lhy.pandaid.util.UsernameGenerator;
import me.lhy.pandaid.util.JwtUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final AuthenticationManager authenticationManager;


    public UserServiceImpl(UserMapper userMapper,
                           PasswordEncoder passwordEncoder,
                           RoleMapper roleMapper,
                           UserRoleMapper userRoleMapper, AuthenticationManager authenticationManager) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleMapper = roleMapper;
        this.userRoleMapper = userRoleMapper;
        this.authenticationManager = authenticationManager;
    }

    private void checkPassword(String password) {
        // 密码强度校验（至少8位，含大小写字母、数字、特殊字符）
        if (!password.matches(Constants.PASSWORD_PATTERN)) {
            throw new PasswordFormatException("密码需包含大小写字母、数字及特殊字符，至少8位");
        }
    }

    private void checkUsername(String username) {
        if (!username.matches(Constants.USERNAME_PATTERN)) {
            throw new UsernameFormatException("用户名只能由数字、字母、下划线和点构成，最长16位");
        }
    }

    private void checkNickname(String nickname) {
        // 用户昵称校验，长度在1-15位之间
        if (nickname.length() > 15) {
            throw new NicknameFormatException("用户昵称超过15字符限制");
        }
    }

    private void checkPhoneNumber(String phoneNumber) {
        if (!phoneNumber.matches(Constants.PHONE_NUMBER_PATTERN)) {
            throw new PhoneFormatException("电话号码不符合格式");
        }
    }

    private void checkGender(Character gender) {
        char g = Character.toUpperCase(gender);
        if (g != 'M' && g != 'F') {
            throw new GenderFormatException("性别格式错误(M/F)");
        }
    }


    @Override
    public String login(String username, String password) {
        try {
            // 进行用户名、密码认证
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            // 认证成功后，SecurityContextHolder 会保存认证信息
            UserDetails userDetails = (UserDetails) authenticationManager.authenticate(authToken).getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authToken);
            // 生成 JWT token
            return JwtUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
           throw new RuntimeException("登录失败");
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
    @CacheEvict(value = {"user", "userCount"}, allEntries = true)
    public void register(RegisterDTO dto) {
        if (dto.getNickname() == null ||
                dto.getPassword() == null ||
                dto.getDeviceInfo() == null) {
            throw new RegisterInfoEmptyException("必要注册信息不能为空");
        }
        // 检查注册信息是否正确
        checkNickname(dto.getNickname());
        checkPassword(dto.getPassword());
        User user = Converter.INSTANCE.toUser(dto);
        // 生成默认username
        String username = UsernameGenerator.generate(dto.getDeviceInfo());
        // 检查用户名是否重复
        User user1 = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user1 != null) {
            throw new DuplicateUsernameException("用户名生成重复");
        }
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
    public List<UserDTO> getAllWithPage(int pageNum, int pageSize) {
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
    public UserDTO getOneById(Long id) {
        if (id == null) throw new IllegalArgumentException("id不能为空");
        UserDTO dto = Converter.INSTANCE.toUserDto(userMapper.selectById(id));
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
    public UserDTO getOneByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("username不能为空");
        UserDTO dto = Converter.INSTANCE.toUserDto(userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username)));
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
    public List<UserDTO> getDeletedWithPage(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        List<User> users = userMapper.selectPage(page, new LambdaQueryWrapper<User>().eq(User::getDeleted, true)).getRecords();
        return users.stream().map(Converter.INSTANCE::toUserDto).toList();
    }

    /**
     * 批量添加用户
     *
     * @param userDTOS 多个用户信息
     */
    @LogOperation(value = "批量添加用户")
    @Transactional
    @Override
    @CacheEvict(value = {"users", "userCount", "deletedUsers"}, allEntries = true)
    public void addMany(List<UserDTO> userDTOS) {
        List<User> users = userDTOS.stream().map(Converter.INSTANCE::toUser).toList();
        userMapper.insert(users);
    }

    /**
     * 更新单个用户
     *
     * @param dto 新用户信息
     */
    @LogOperation(value = "更新单个用户")
    @Transactional
    @Override
    @CacheEvict(value = {"user", "users"}, allEntries = true)
    public void updateOneByUsername(UserDTO dto) {

        if (!dto.getUsername().isBlank()) {
            checkUsername(dto.getUsername());
        }
        if (!dto.getNickname().isBlank()) {
            checkNickname(dto.getNickname());
        }
        if (!dto.getPhoneNumber().isBlank()) {
            checkPhoneNumber(dto.getPhoneNumber());

        }
        if (dto.getGender() != null) {
            checkGender(dto.getGender());
        }
        User user = Converter.INSTANCE.toUser(dto);
        // 根据用户名更新用户
        userMapper.update(user, new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
    }

    @Transactional
    @CacheEvict(value = {"user", "users", "userCount"}, allEntries = true)
    @Override
    public void deleteOneByUsername(String username) {
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new UserNotFoundException("用户不存在");
        }
        // user表中删除用户
        userMapper.deleteById(user.getId());
        // user_role表中删除用户与所有角色的关联
        userRoleMapper
                .delete(new LambdaQueryWrapper<UserRole>()
                        .eq(UserRole::getUserId, user.getId()));
    }
}
