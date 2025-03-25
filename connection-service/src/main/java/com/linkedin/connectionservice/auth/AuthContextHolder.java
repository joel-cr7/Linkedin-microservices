package com.linkedin.connectionservice.auth;

public class AuthContextHolder {

    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<Long>();

    public static Long getCurrentUserId(){
        return currentUserId.get();
    }

    static void setCurrentUserId(Long userId){
        currentUserId.set(userId);
    }

    static void clear(){
        currentUserId.remove();
    }
}
