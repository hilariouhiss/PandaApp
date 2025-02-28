package me.lhy.pandaid.controller;

import io.swagger.v3.oas.annotations.Operation;
import me.lhy.pandaid.domain.dto.RegisterDto;
import me.lhy.pandaid.service.UserService;
import me.lhy.pandaid.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {this.userService = userService;}

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDto registerDto) {
        userService.register(registerDto);
        return Result.success();
    }
}
