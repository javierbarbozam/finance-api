package com.finance.finance_api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.finance.finance_api.repository.UserRepository;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"))
        ;
    }
}
