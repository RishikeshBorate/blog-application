package com.project.blog_application.controllers;

import com.project.blog_application.dtos.PostRequestDto;
import com.project.blog_application.models.Post;
import com.project.blog_application.services.PostService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PostController {
    private PostService postService ;

    public PostController(@Qualifier("PostServiceImpl") PostService postService){
        this.postService = postService ;
    }

    @GetMapping("/newpost")
    public String createBlogForm(Model model){
        PostRequestDto postRequestDto = new PostRequestDto() ;
        model.addAttribute("post" , postRequestDto) ;

        return "newpost" ;
    }

    @PostMapping("/newpost")
    public String createBlog(@ModelAttribute PostRequestDto postRequestDto){
       postService.createBlog(postRequestDto);
       return "redirect:/newpost" ;
    }

    @GetMapping("/")
    public String getAllPosts(Model model){
        List<Post> postList = postService.getAllPosts();
        model.addAttribute("postList" , postList) ;
        model.addAttribute("post" , new Post()) ;
        return "dashboard" ;
    }

    @GetMapping("/post/{postId}")
    public String getPostById(@PathVariable("postId") Long id , Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post" , post);
        return "post" ;
    }
}
