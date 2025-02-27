package me.lhy.pandaid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.lhy.pandaid.domain.dto.LoginDto;
import me.lhy.pandaid.domain.dto.RegisterDto;
import me.lhy.pandaid.domain.dto.UserDto;
import me.lhy.pandaid.service.UserService;
import me.lhy.pandaid.util.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<Void> login(@RequestBody LoginDto loginDto) {
        userService.login(loginDto);
        return Result.success();
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return Result.success();
    }

    @Operation(summary = "获取所有用户")
    @GetMapping("/getAll")
    public Result<List<UserDto>> getAllUsers() {
        var users = userService.getAll();
        return Result.success(users);
    }

    @Operation(summary = "根据ID获取单个用户")
    @GetMapping("/getOneById")
    public Result<UserDto> getOneById(@RequestParam Long id) {
        var user = userService.getOneById(id);
        return Result.success(user);
    }

    @Operation(summary = "根据用户名获取单个用户")
    @GetMapping("/getOneByUsername")
    public Result<UserDto> getOneByUsername(@RequestParam String username) {
        var user = userService.getOneByUsername(username);
        return Result.success(user);
    }

    @Operation(summary = "获取用户总数")
    @GetMapping("/getCount")
    public Result<Long> getCount() {
        var count = userService.getCount();
        return Result.success(count);
    }

    @Operation(summary = "获取已删除的用户")
    @GetMapping("/getDeleted")
    public Result<List<UserDto>> getDeleted() {
        var users = userService.getDeleted();
        return Result.success(users);
    }

    @Operation(summary = "添加一个用户")
    @PostMapping("/addOne")
    public Result<Void> addOne(@RequestBody UserDto userDto) {
        userService.addOne(userDto);
        return Result.success();
    }

    @Operation(summary = "添加多个用户")
    @PostMapping("/addMany")
    public Result<Void> addMany(@RequestBody List<UserDto> userDtos) {
        userService.addMany(userDtos);
        return Result.success();
    }

    @Operation(summary = "更新一个用户")
    @PutMapping("/updateOne")
    public Result<Void> updateOne(@RequestBody UserDto userDto) {
        userService.updateOne(userDto);
        return Result.success();
    }

    @Operation(summary = "根据ID删除一个用户")
    @DeleteMapping("/deleteOneById")
    public Result<Void> deleteOneById(@RequestParam Long id) {
        userService.deleteOneById(id);
        return Result.success();
    }

    @Operation(summary = "根据用户名删除一个用户")
    @DeleteMapping("/deleteOneByUsername")
    public Result<Void> deleteOneByUsername(@RequestParam String username) {
        userService.deleteOneByUsername(username);
        return Result.success();
    }

    @Operation(summary = "根据ID删除多个用户")
    @DeleteMapping("/deleteMany")
    public Result<Void> deleteMany(@RequestBody List<Long> ids) {
        userService.deleteMany(ids);
        return Result.success();
    }
}