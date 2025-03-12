package me.lhy.pandaid.exception;

public class UserRoleAlreadyExistedException extends RuntimeException{

    public UserRoleAlreadyExistedException(String message) {
        super(message);
    }
}
