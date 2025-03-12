package me.lhy.pandaid.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import me.lhy.pandaid.domain.dto.UserDTO;
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

    @Operation(summary = "获取所有用户")
    @GetMapping("/getAll")
    public Result<List<UserDTO>> getAllUsers(@RequestParam int pageNum, @RequestParam int pageSize) {
        var users = userService.getAllWithPage(pageNum, pageSize);
        return Result.success(users);
    }

    @Operation(summary = "根据ID获取单个用户")
    @GetMapping("/getOneById")
    public Result<UserDTO> getOneById(@RequestParam Long id) {
        var user = userService.getOneById(id);
        return Result.success(user);
    }

    @Operation(summary = "根据用户名获取单个用户")
    @GetMapping("/getOneByUsername")
    public Result<UserDTO> getOneByUsername(@RequestParam String username) {
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
    public Result<List<UserDTO>> getDeleted(@RequestParam int pageNum, @RequestParam int pageSize) {
        var users = userService.getDeletedWithPage(pageNum, pageSize);
        return Result.success(users);
    }

    @Operation(summary = "添加多个用户")
    @PostMapping("/addMany")
    public Result<Void> addMany(@RequestBody List<UserDTO> userDTOS) {
        userService.addMany(userDTOS);
        return Result.success();
    }

    @Operation(summary = "更新一个用户")
    @PutMapping("/updateOne")
    public Result<Void> updateOne(@RequestBody UserDTO userDto) {
        userService.updateOneByUsername(userDto);
        return Result.success();
    }

    @Operation(summary = "根据用户名删除一个用户")
    @DeleteMapping("/deleteOneByUsername")
    public Result<Void> deleteOneByUsername(@RequestParam String username) {
        userService.deleteOneByUsername(username);
        return Result.success();
    }
}