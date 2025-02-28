package me.lhy.pandaid.exception;

public class UserAlreadyExistsException extends RuntimeException{

    // 无详细信息
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
