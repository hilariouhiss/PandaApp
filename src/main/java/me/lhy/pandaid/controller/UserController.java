package me.lhy.pandaid.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import me.lhy.pandaid.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // @PostMapping("/login")
    // public String login(@RequestBody UserDto userDto) {
    //     return userService.login(userDto);
    // }
}
