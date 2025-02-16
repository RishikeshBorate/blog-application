package com.project.blog_application.services;

import com.project.blog_application.models.Comment;

import java.util.Optional;

public interface CommentService {
    void addComment(Long id , String user , String commentText) ;
    Optional<Comment> findCommentById(Long id) ;
    void saveSubComment(Long commentId , Long postId , String user , String commentText);
}
