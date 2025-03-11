package com.linkedin.userservice.service;

import com.linkedin.userservice.entity.User;

public interface JWTService {
    String generateAccessToken(User user);
}
