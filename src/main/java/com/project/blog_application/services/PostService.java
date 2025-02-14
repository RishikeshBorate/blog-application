package com.project.blog_application.services;

import com.project.blog_application.dtos.PostRequestDto;
import com.project.blog_application.models.Post;
import com.project.blog_application.models.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostService {
    public void createBlog(PostRequestDto postRequestDto);
    List<Post> getAllPosts();
    Post getPostById(Long id) ;
    void updateBlog(PostRequestDto postRequestDto);
    Page<Post> findAllPaginatedPost(int pageNo , int pageSize) ;
    List<Tag> getTagList();
    Page<Post> findFilteredPosts(Long authorId, List<Long> tagIds, Boolean isPublished,
                                 LocalDateTime startDate, LocalDateTime endDate, Pageable pageable , String sortBy, String sortOrder) ;
}
