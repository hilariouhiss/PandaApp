package me.lhy.pandaid.exception;

public class NicknameTooLongException extends RuntimeException {
    public NicknameTooLongException(String message) {
        super(message);
    }
}
