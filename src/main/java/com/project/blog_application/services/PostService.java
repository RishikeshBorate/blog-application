package com.project.blog_application.services;

import com.project.blog_application.dtos.PostRequestDto;
import com.project.blog_application.models.Post;
import com.project.blog_application.models.Tag;
import com.project.blog_application.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostService {
    public void createBlog(PostRequestDto postRequestDto , String userName);
    List<Post> getAllPosts();
    Post getPostById(Long id) ;
    void updateBlog(PostRequestDto postRequestDto);
    Page<Post> findAllPaginatedPost(int pageNo , int pageSize) ;
    List<Tag> getTagList();
    Page<Post> findFilteredPosts(List<Long> authorId, List<Long> tagIds, Boolean isPublished,
                                 LocalDateTime startDate, LocalDateTime endDate, Pageable pageable , String sortBy, String sortOrder , String search) ;

    void deletePost(Long postId);
}
