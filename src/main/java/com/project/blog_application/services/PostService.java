package com.project.blog_application.services;

import com.project.blog_application.dtos.PostRequestDto;

public interface PostService {
    public void createBlog(PostRequestDto postRequestDto);
}
