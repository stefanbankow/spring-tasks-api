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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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
    final
    BCryptPasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @PostMapping("/signup")
    public ResponseEntity<Map> signup(@RequestBody SignUpRequest body) {
        String username = body.getUsername();
        String email = body.getEmail();
        User user = userRepository.save(User.builder().username(username)
                .password(passwordEncoder.encode(body.getPassword()))
                .email(email).roles(Arrays.asList("ROLE_USER")).build());
        Map<Object, Object> response = new HashMap<>();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, body.getPassword()));
        String token = jwtTokenProvider.createToken(username, user.getRoles());
        response.put("username", username);
        response.put("email", email);
        response.put("roles", user.getRoles());
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
