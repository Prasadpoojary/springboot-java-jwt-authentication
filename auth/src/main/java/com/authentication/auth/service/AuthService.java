package com.authentication.auth.service;

import com.authentication.auth.constant.MessageStatus;
import com.authentication.auth.dto.Message;
import com.authentication.auth.model.User;
import com.authentication.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService
{
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    public Optional<User> findUserByEmail(String email)
    {
        Optional<User> usr=userRepository.findByEmail(email);
        return usr;
    }

    public Message addUser(User user)
    {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return new Message(MessageStatus.SUCCESS,"User registered successfully");
    }

    public List<User> allUser()
    {
        return userRepository.findAll();
    }

    public Optional<User> findUser(int id)
    {
        return userRepository.findById(id);
    }

    public Boolean deleteUser(int id)
    {
        try
        {
            userRepository.deleteById(id);
            return true;
        }
        catch(Exception e)
        {
            return false;
        }

    }

}
