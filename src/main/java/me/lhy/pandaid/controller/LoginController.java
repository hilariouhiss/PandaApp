package me.lhy.pandaid.controller;

import me.lhy.pandaid.domain.dto.LoginDTO;
import me.lhy.pandaid.service.UserService;
import me.lhy.pandaid.util.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {this.userService = userService;}

    @PostMapping
    public Result<String> login(@RequestBody LoginDTO dto){
        String token = userService.login(dto.getUsername(), dto.getPassword());
        return Result.success(token);
    }
}
