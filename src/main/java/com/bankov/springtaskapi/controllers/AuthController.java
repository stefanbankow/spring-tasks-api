package com.bankov.springtaskapi.controllers;

import com.bankov.springtaskapi.components.JwtTokenProvider;
import com.bankov.springtaskapi.dtos.LoginRequest;
import com.bankov.springtaskapi.dtos.SignUpRequest;
import com.bankov.springtaskapi.models.User;
import com.bankov.springtaskapi.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    final
    AuthenticationManager authenticationManager;
    final
    JwtTokenProvider jwtTokenProvider;
    final
    UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity<Map> signin(@RequestBody LoginRequest body) {
        String username = body.getUsername();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, body.getPassword()));
        String token = jwtTokenProvider.createToken(username, userRepository.
                findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("Username not found in signin")).
                getRoles());

        Map<Object, Object> responseBody = new HashMap<>();
        responseBody.put("username", username);
        responseBody.put("token", token);

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("signup")
    public ResponseEntity<Map> signup(@RequestBody SignUpRequest body) {
        Map<String, String> map = new HashMap<>();
        return ResponseEntity.ok(map);
    }
}
