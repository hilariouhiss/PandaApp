package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.lhy.pandaid.domain.dto.RegisterDto;
import me.lhy.pandaid.domain.dto.UserDto;
import me.lhy.pandaid.domain.po.User;
import me.lhy.pandaid.exception.UserAlreadyExistsException;
import me.lhy.pandaid.mapper.UserMapper;
import me.lhy.pandaid.service.UserService;
import me.lhy.pandaid.util.Converter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper mapper, PasswordEncoder passwordEncoder) {this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 根据用户名加载用户信息
     * @param username 用户名
     * @return 用户信息
     * @throws UsernameNotFoundException 用户不存在
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var wrapper = new LambdaQueryWrapper<User>().eq(User::getUsername, username);
        User user = mapper.selectOne(wrapper);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        log.info("加载用户：{}", user.getUsername());
        return Converter.INSTANCE.toSecurityUser(user);
    }

    /**
     * 注册用户
     * @param dto 注册信息
     * @return 注册结果
     */
    @Override
    public Boolean register(RegisterDto dto) {
        // 判断用户名是否已存在
        if (mapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername())) != null) {
            log.info("{}已存在", dto.getUsername());
            throw new UserAlreadyExistsException("用户已存在");
        }
        // 加密密码
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        // 插入用户
        // todo 插入默认角色
        int count = mapper.insert(Converter.INSTANCE.toUser(dto));
        log.info("{} 注册", dto.getUsername());
        return count > 0;
    }

    /**
     * 分页查询所有用户
     * @param pageNum 页码
     * @param pageSize 页大小
     * @return 指定页的用户
     */
    @Override
    public List<UserDto> getAllWithPage(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        List<User> users = mapper.selectPage(page, null).getRecords();
        log.info("第 {} 页， {} 位用户", pageNum, pageSize);
        return users.stream().map(Converter.INSTANCE::toUserDto).toList();
    }

    /**
     * 根据id获取用户
     * @param id 用户id
     * @return 用户信息
     */
    @Override
    public UserDto getOneById(Long id) {
        log.info("获取用户 {}", id);
        return Converter.INSTANCE.toUserDto(mapper.selectById(id));
    }

    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户信息
     */
    @Override
    public UserDto getOneByUsername(String username) {
        log.info("获取用户 {}", username);
        return Converter.INSTANCE.toUserDto(mapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username)));
    }

    /**
     * 获取用户总数
     * @return 用户总数
     */
    @Override
    public Long getCount() {
        log.info("获取用户总数");
        return mapper.selectCount(null);
    }

    /**
     * 获取已删除的用户
     * @return 已删除的用户
     */
    @Override
    public List<UserDto> getDeletedWithPage(int pageNum, int pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        List<User> users = mapper.selectPage(page, new LambdaQueryWrapper<User>().eq(User::getDeleted, true)).getRecords();
        log.info("第 {} 页，{} 个被删除用户",pageNum, pageSize);
        return users.stream().map(Converter.INSTANCE::toUserDto).toList();
    }

    /**
     * 批量添加用户
     * @param userDtos 多个用户信息
     */
    @Transactional
    @Override
    public void addMany(List<UserDto> userDtos) {
        log.info("批量添加用户，共 {} 条数据", userDtos.size());
        List<User> users = userDtos.stream().map(Converter.INSTANCE::toUser).toList();
        mapper.insert(users);
    }

    /**
     * 更新单个用户
     * @param userDto 新用户信息
     */
    @Transactional
    @Override
    public void updateOne(UserDto userDto) {
        log.info("更新用户 {}", userDto.getUsername());
        mapper.updateById(Converter.INSTANCE.toUser(userDto));
    }

    /**
     * 根据id删除用户
     * @param id 用户id
     */
    @Transactional
    @Override
    public void deleteOneById(Long id) {
        log.info("删除用户 {}", id);
        mapper.deleteById(id);
    }

    /**
     * 根据用户名删除用户
     * @param username 用户名
     */
    @Transactional
    @Override
    public void deleteOneByUsername(String username) {
        log.info("删除用户 {}", username);
        mapper.delete(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    /**
     * 批量删除用户
     * @param ids 用户id列表
     */
    @Transactional
    @Override
    public void deleteMany(List<Long> ids) {
        log.info("批量删除用户，共 {} 条数据", ids.size());
        mapper.deleteByIds(ids);
    }
}
