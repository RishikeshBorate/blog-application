package com.project.blog_application.controllers;

import com.project.blog_application.models.Comment;
import com.project.blog_application.models.Post;
import com.project.blog_application.models.User;
import com.project.blog_application.services.CommentService;
import com.project.blog_application.services.PostService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class CommentController {

    private CommentService commentService;
    private PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/comment/reply/{commentId}")
    public String saveSubComment(@PathVariable("commentId") Long commentId, @RequestParam("postId") Long postId, @RequestParam("userName") String user, @RequestParam("commentText") String commentText) {
        commentService.saveSubComment(commentId, postId, user, commentText);
        System.out.println("Post Id is : " + postId);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/comment/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId, @RequestParam("postId") Long postId, Authentication authentication) {
        String loggedInUser = authentication.getName();
        Post post = postService.getPostById(postId);
        User user = post.getAuthor();

        boolean isOwner = user.equals(loggedInUser) ;
        boolean access = ( isOwner || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) ;

        if (!access) {
            throw new AccessDeniedException("You are not allowed to delete this comment.");
        }

        commentService.deleteComment(commentId);
        return "redirect:/post/" + postId;
    }

    @GetMapping("/comment/edit/{commentId}")
    public String editComment(@PathVariable("commentId") Long commentId, Model model, Authentication authentication) {
        Optional<Comment> commentOptional = commentService.findCommentById(commentId);
        String loggedInUser = authentication.getName();

        Post post = commentOptional.get().getPost();
        User postAuthor = post.getAuthor();

        boolean isOwner = postAuthor.equals(loggedInUser) ;
        boolean access = ( isOwner || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) ;

        if (!access) {
            throw new AccessDeniedException("You are not allowed to edit this comment.");
        }

        model.addAttribute("comment", commentOptional.get());

        return "editcomment";
    }

    @PostMapping("/comment/edit/{commentId}")
    public String editComment(@PathVariable("commentId") Long commentId, Authentication authentication, @ModelAttribute("comment") Comment comment) {
        Optional<Comment> commentOptional = commentService.findCommentById(commentId);

        String loggedInUser = authentication.getName();
        Post post = commentOptional.get().getPost();
        User user = post.getAuthor();

        boolean isOwner = user.equals(loggedInUser) ;
        boolean access = ( isOwner || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) ;

        if (!access) {
            throw new AccessDeniedException("You are not allowed to delete this comment.");
        }

        commentService.editComment(comment);

        return "redirect:/post/" + post.getId();
    }

}
