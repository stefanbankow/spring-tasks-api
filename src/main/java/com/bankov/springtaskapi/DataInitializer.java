/*
package com.bankov.springtaskapi;

import com.bankov.springtaskapi.models.User;
import com.bankov.springtaskapi.repos.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class DataInitializer implements CommandLineRunner {
    final
    BCryptPasswordEncoder bCryptPasswordEncoder;
    final
    UserRepository userRepository;

    public DataInitializer(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        userRepository.save(User.builder().email("user@gmail.com")
                .username("user")
                .password(bCryptPasswordEncoder.encode("password")).roles(Arrays.asList("ROLE_USER"))
                .build());
    }
}
*/
