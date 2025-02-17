package com.project.blog_application.services;

import com.project.blog_application.dtos.UserRequestDto;
import com.project.blog_application.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void addNewUser(UserRequestDto userRequestDto) ;
    List<User> findAllUsers() ;
    Optional<User> findUserByEmail(String emailId) ;

}
