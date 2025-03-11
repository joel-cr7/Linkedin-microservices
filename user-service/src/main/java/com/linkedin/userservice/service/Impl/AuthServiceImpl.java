package com.linkedin.userservice.service.Impl;

import com.linkedin.userservice.dto.LoginRequestDTO;
import com.linkedin.userservice.dto.SignupRequestDTO;
import com.linkedin.userservice.dto.UserDTO;
import com.linkedin.userservice.entity.User;
import com.linkedin.userservice.exception.BadRequestException;
import com.linkedin.userservice.exception.ResourceNotFoundException;
import com.linkedin.userservice.repository.UserRepository;
import com.linkedin.userservice.service.AuthService;
import com.linkedin.userservice.service.JWTService;
import com.linkedin.userservice.utils.BCrypt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JWTService jwtService;
    private final ModelMapper modelMapper;


    @Override
    public UserDTO createUser(SignupRequestDTO signupRequestDTO) {
        String userEmail = signupRequestDTO.getEmail();
        log.info("Signup a user with email: {}", userEmail);

        boolean userAlreadyExists = userRepository.existsByEmail(userEmail);
        if(userAlreadyExists) throw new BadRequestException("User already exists !!");

        User user = modelMapper.map(signupRequestDTO, User.class);
        user.setPassword(BCrypt.hash(signupRequestDTO.getPassword()));

        return modelMapper.map(userRepository.save(user), UserDTO.class);
    }

    @Override
    public String loginUser(LoginRequestDTO loginRequestDTO) {
        String userEmail = loginRequestDTO.getEmail();
        log.info("Login request for user with email: {}", userEmail);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new BadRequestException("Incorrect email or password !!"));

        boolean isPasswordMatch = BCrypt.match(loginRequestDTO.getPassword(), user.getPassword());
        if(!isPasswordMatch){
            throw new BadRequestException("Incorrect email or password !!");
        }

        return jwtService.generateAccessToken(user);
    }
}
