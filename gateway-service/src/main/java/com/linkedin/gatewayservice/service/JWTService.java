package com.linkedin.gatewayservice.service;

public interface JWTService {
    String getUserIdFromToken(String token);
}
