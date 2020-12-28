package com.bankov.springtaskapi.services;

import com.bankov.springtaskapi.models.User;
import com.bankov.springtaskapi.repos.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    final
    UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("No user with this username " + username));
        //This initialization is required because otherwise Spring Persistence throws an LazyInitializationException
        Hibernate.initialize(user.getTasks());
        return user;
    }

}
