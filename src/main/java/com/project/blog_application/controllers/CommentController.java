package com.project.blog_application.controllers;

import com.project.blog_application.models.Comment;
import com.project.blog_application.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class CommentController {

    private CommentService commentService ;

    public CommentController(CommentService commentService){
        this.commentService = commentService ;
    }

    @PostMapping("/comment/reply/{commentId}")
    public String saveSubComment(@PathVariable("commentId")Long commentId ,@RequestParam("postId")Long postId , @RequestParam("userName")String user , @RequestParam("commentText")String commentText){
          commentService.saveSubComment(commentId,postId, user, commentText);
          System.out.println("Post Id is : " + postId);
          return "redirect:/post/" + postId ;
    }

}
