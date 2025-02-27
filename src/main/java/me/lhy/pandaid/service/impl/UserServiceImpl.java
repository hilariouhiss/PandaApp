package me.lhy.pandaid.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import me.lhy.pandaid.domain.dto.LoginDto;
import me.lhy.pandaid.domain.dto.RegisterDto;
import me.lhy.pandaid.domain.dto.UserDto;
import me.lhy.pandaid.domain.po.User;
import me.lhy.pandaid.mapper.UserMapper;
import me.lhy.pandaid.service.UserService;
import me.lhy.pandaid.util.Converter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserMapper mapper;

    public UserServiceImpl(UserMapper mapper) {this.mapper = mapper;}


    @Override
    public Boolean login(LoginDto dto) {
        User user = mapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, dto.getUsername())
                .eq(User::getPassword, dto.getPassword()));
        return user != null;
    }

    @Override
    public Boolean register(RegisterDto dto) {
        int count = mapper.insert(Converter.INSTANCE.toUser(dto));
        return count > 0;
    }

    @Override
    public List<UserDto> getAll() {
        return mapper.selectList(null)
                     .stream().map(Converter.INSTANCE::toUserDto)
                     .toList();
    }

    @Override
    public UserDto getOneById(Long id) {
        return Converter.INSTANCE.toUserDto(mapper.selectById(id));
    }

    @Override
    public UserDto getOneByUsername(String username) {
        return Converter.INSTANCE.toUserDto(mapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username)));
    }

    @Override
    public Long getCount() {
        return mapper.selectCount(null);
    }

    @Override
    public List<UserDto> getDeleted() {
        return mapper.selectList(new LambdaQueryWrapper<User>().eq(User::getDeleted, true))
                     .stream().map(Converter.INSTANCE::toUserDto)
                     .toList();
    }

    @Transactional
    @Override
    public void addOne(UserDto userDto) {
        mapper.insert(Converter.INSTANCE.toUser(userDto));
    }

    @Transactional
    @Override
    public void addMany(List<UserDto> userDtos) {
        List<User> users = userDtos.stream().map(Converter.INSTANCE::toUser).toList();
        mapper.insert(users);
    }

    @Transactional
    @Override
    public void updateOne(UserDto userDto) {
        mapper.updateById(Converter.INSTANCE.toUser(userDto));
    }

    @Transactional
    @Override
    public void deleteOneById(Long id) {
        mapper.deleteById(id);
    }

    @Transactional
    @Override
    public void deleteOneByUsername(String username) {
        mapper.delete(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
    }

    @Transactional
    @Override
    public void deleteMany(List<Long> ids) {
        mapper.deleteByIds(ids);
    }
}
