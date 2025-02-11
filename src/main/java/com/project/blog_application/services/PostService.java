package com.project.blog_application.services;

import com.project.blog_application.dtos.PostRequestDto;
import com.project.blog_application.models.Post;

import java.util.List;

public interface PostService {
    public void createBlog(PostRequestDto postRequestDto);
    List<Post> getAllPosts();
    Post getPostById(Long id) ;
}
