package com.linkedin.userservice.service;

import com.linkedin.userservice.dto.LoginRequestDTO;
import com.linkedin.userservice.dto.SignupRequestDTO;
import com.linkedin.userservice.dto.UserDTO;

public interface AuthService {
    UserDTO createUser(SignupRequestDTO signupRequestDTO);

    String loginUser(LoginRequestDTO loginRequestDTO);
}
