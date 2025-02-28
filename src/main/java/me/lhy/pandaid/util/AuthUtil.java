package me.lhy.pandaid.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {

    public static String getCurrentUser() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
