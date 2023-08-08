package com.authentication.auth.config;

import com.authentication.auth.model.User;
import com.authentication.auth.repository.UserRepository;
import com.authentication.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoDetailService implements UserDetailsService
{
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user=userRepository.findByEmail(email);
        return user.map(UserInfoDetails::new).orElseThrow(()->new UsernameNotFoundException("Invalid Username"));
    }
}
