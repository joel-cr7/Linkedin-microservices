package com.linkedin.userservice.controller;


import com.linkedin.userservice.dto.LoginRequestDTO;
import com.linkedin.userservice.dto.SignupRequestDTO;
import com.linkedin.userservice.dto.UserDTO;
import com.linkedin.userservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> createUser(@RequestBody SignupRequestDTO signupRequestDTO){
        UserDTO userDTO = authService.createUser(signupRequestDTO);
        return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        String token = authService.loginUser(loginRequestDTO);
        return ResponseEntity.ok(token);
    }

}
