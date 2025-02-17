package com.project.blog_application.controllers;

import com.project.blog_application.dtos.PostRequestDto;
import com.project.blog_application.dtos.PostToPostRequestDtoMapper;
import com.project.blog_application.models.Post;
import com.project.blog_application.models.Tag;
import com.project.blog_application.models.User;
import com.project.blog_application.services.CommentService;
import com.project.blog_application.services.PostService;
import com.project.blog_application.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PostController {
    private PostService postService ;
    private CommentService commentService ;
    private UserService userService ;

    public PostController(@Qualifier("PostServiceImpl") PostService postService , CommentService commentService , UserService userService){
        this.postService = postService ;
        this.commentService = commentService ;
        this.userService = userService ;
    }

    @GetMapping("/newpost")
    public String createBlogForm(Model model, Authentication authentication){
        String userName = authentication.getName();
        Optional<User> userOptional = userService.findUserByEmail(userName) ;

        String role = userOptional.get().getRole() ;

        PostRequestDto postRequestDto = new PostRequestDto() ;
        model.addAttribute("post" , postRequestDto) ;
        model.addAttribute("role" , role) ;
        model.addAttribute("authorName" , userOptional.get().getName()) ;
        return "newpost" ;
    }

    @PostMapping("/newpost")
    public String createBlog(@ModelAttribute PostRequestDto postRequestDto ,Authentication authentication){
        String userName = authentication.getName();
       postService.createBlog(postRequestDto , userName);
       return "redirect:/" ;
    }

    @GetMapping("/")
    public String getAllPosts(@RequestParam(value = "authorId" , required = false) List<Long> authorId ,
                              @RequestParam(value = "tagId" , required = false) List<Long> tagIds ,
                              @RequestParam(value = "isPublished" , required = false) Boolean isPublished ,
                              @RequestParam(value = "startDate" , required = false) LocalDateTime startDate ,
                              @RequestParam(value = "endDate" , required = false) LocalDateTime endDate ,
                              @RequestParam(value = "search" , required = false ) String search ,
                              @RequestParam(value = "sortBy" , required = false , defaultValue = "createdAt") String sortBy ,
                              @RequestParam(value = "sortOrder" , required = false , defaultValue = "desc") String sortOrder ,
                              @RequestParam(value = "start" , required = false , defaultValue = "1") Integer start ,
                              @RequestParam(value = "limit" , required = false , defaultValue = "2") Integer limit ,
                              Model model){


       int pageNo = start.intValue() ;
       int totalItems = limit.intValue() ;

        Pageable pageable = PageRequest.of(pageNo - 1 , limit) ;

        Page<Post> postPage=  postService.findFilteredPosts(authorId, tagIds,  isPublished,  startDate,  endDate,  pageable,  sortBy ,  sortOrder , search) ;

        List<Post> postList = postPage.getContent() ;
        List<Tag> tagsList = postService.getTagList() ;
        List<User> userList =  userService.findAllUsers() ;

        model.addAttribute("currentPage" , pageNo) ;
        model.addAttribute("limit" , totalItems) ;
        model.addAttribute("totalPages" , postPage.getTotalPages()) ;
        model.addAttribute("totalItems" , postPage.getTotalElements()) ;
        model.addAttribute("postList" , postList) ;
        model.addAttribute("userList" , userList) ;
        model.addAttribute("tagList" , tagsList) ;
        model.addAttribute("tagId" , tagIds) ;
        model.addAttribute("authorId" , authorId) ;
        model.addAttribute("isPublished" , isPublished) ;
        model.addAttribute("startDate" , startDate) ;
        model.addAttribute("endDate" , startDate) ;
        model.addAttribute("search" , search) ;
        model.addAttribute("sortBy" , sortBy) ;
        model.addAttribute("sortOrder" , sortOrder) ;
        model.addAttribute("start" , start) ;
        model.addAttribute("limit" , limit) ;

        return "paginatedDashboard" ;
    }


    @GetMapping("/post/{postId}")
    public String getPostById(@PathVariable("postId") Long id , Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post" , post);
        return "postwithcomment" ;
    }

    @GetMapping("edit/{postId}")
    public String editPostForm(@PathVariable("postId") Long id , Model model , Authentication authentication){
        String loggedInUser = authentication.getName() ;
        Post post = postService.getPostById(id);

        String author = post.getAuthor().getEmail() ;

        boolean isOwner = author.equals(loggedInUser) ;
        boolean access = ( isOwner || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) ;

        if (!access) {
            throw new AccessDeniedException("You are not allowed to edit this post.");
        }

        PostToPostRequestDtoMapper mapper = new PostToPostRequestDtoMapper() ;
        PostRequestDto postRequestDto = mapper.mapPostToPostRequestDto(post); ;
        model.addAttribute("post" , postRequestDto);

        return "editpost" ;
    }

    @PostMapping("/editpost")
    public String editPost(@ModelAttribute("post") PostRequestDto postRequestDto , Authentication authentication){
        String loggedInUser = authentication.getName() ;
        Post post = postService.getPostById(postRequestDto.getId());
        String author = post.getAuthor().getEmail() ;

        boolean isOwner = author.equals(loggedInUser) ;
        boolean access = ( isOwner || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) ;

        if (!access) {
            throw new AccessDeniedException("You are not allowed to edit this post.");
        }

        postService.updateBlog(postRequestDto);

        return "redirect:/" ;
    }

    @PostMapping("/comment/{id}")
    public String addComment(@PathVariable("id") Long postId , @RequestParam("userName") String user , @RequestParam("commentText")String commentText){
          commentService.addComment(postId, user, commentText);
          return "redirect:/post/" + postId ;
    }

    @GetMapping("/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long postId , Authentication authentication){
        String loggedInUser = authentication.getName() ;
        Post post = postService.getPostById(postId);
        String author = post.getAuthor().getEmail() ;

        boolean isOwner = author.equals(loggedInUser) ;
        boolean access = ( isOwner || authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")) ) ;

        if (!access) {
            throw new AccessDeniedException("You are not allowed to delete this post.");
        }

        postService.deletePost(postId) ;

        return "redirect:/" ;
    }
}
