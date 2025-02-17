package com.project.blog_application.services;

import com.project.blog_application.dtos.UserRequestDto;
import com.project.blog_application.models.User;
import com.project.blog_application.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        Optional<User> userOptional = userRepository.findByEmail(userRequestDto.getEmail()) ;

        if(!userOptional.isEmpty()){
            throw new RuntimeException("user with same emailId already exist") ;
        }

        User user = new User() ;
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setRole("ROLE_AUTHOR");
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
        userRepository.save(user) ;
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll() ;
    }

    @Override
    public Optional<User> findUserByEmail(String emailId) {
        return userRepository.findByEmail(emailId) ;
    }
}
