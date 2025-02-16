package com.project.blog_application.services;

import com.project.blog_application.dtos.UserRequestDto;
import com.project.blog_application.models.User;
import com.project.blog_application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository ;
    }

    @Override
    public void addNewUser(UserRequestDto userRequestDto) {

        User user = new User() ;
        user.setName(userRequestDto.getName());
        user.setRole(userRequestDto.getRole());
        user.setEmail(userRequestDto.getEmail());
        user.setRole("ROLE_AUTHOR");
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
        userRepository.save(user) ;

    }
}
