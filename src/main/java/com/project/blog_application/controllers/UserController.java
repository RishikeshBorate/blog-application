package com.project.blog_application.controllers;

import com.project.blog_application.dtos.UserRequestDto;
import com.project.blog_application.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private UserService userService ;

    public UserController(UserService userService){
        this.userService = userService ;
    }

    @GetMapping("/signup")
    public String signUpForm(Model model){
        UserRequestDto userRequestDto = new UserRequestDto() ;
        model.addAttribute("userDto" , userRequestDto) ;
        return "signup" ;
    }

    @PostMapping("/signup")
    public String saveUser(@ModelAttribute UserRequestDto userRequestDto){
        userService.addNewUser(userRequestDto);
        return "redirect:/login" ;
    }

}
