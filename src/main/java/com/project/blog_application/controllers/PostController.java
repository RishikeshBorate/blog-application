package com.project.blog_application.controllers;

import com.project.blog_application.dtos.PostRequestDto;
import com.project.blog_application.dtos.PostToPostRequestDtoMapper;
import com.project.blog_application.models.Post;
import com.project.blog_application.models.Tag;
import com.project.blog_application.services.CommentService;
import com.project.blog_application.services.PostService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PostController {
    private PostService postService ;
    private CommentService commentService ;

    public PostController(@Qualifier("PostServiceImpl") PostService postService , CommentService commentService){
        this.postService = postService ;
        this.commentService = commentService ;
    }

    @GetMapping("/newpost")
    public String createBlogForm(Model model){
        PostRequestDto postRequestDto = new PostRequestDto() ;
        model.addAttribute("post" , postRequestDto) ;
        return "newpost" ;
    }

    @PostMapping("/newpost")
    public String createBlog(@ModelAttribute PostRequestDto postRequestDto ,Authentication authentication){
        String userName = authentication.getName();
       postService.createBlog(postRequestDto , userName);
       return "redirect:/" ;
    }

    @GetMapping("/")
    public String getAllPosts(@RequestParam(value = "author" , required = false) Long authorId ,
                              @RequestParam(value = "tagId" , required = false) List<Long> tagIds ,
                              @RequestParam(value = "isPublished" , required = false) Boolean isPublished ,
                              @RequestParam(value = "startDate" , required = false) String startDate ,
                              @RequestParam(value = "startDate" , required = false) String endDate ,
                              @RequestParam(value = "search" , required = false ) String search ,
                              @RequestParam(value = "sortBy" , required = false) String sortBy ,
                              @RequestParam(value = "sortOrder" , required = false) String sortOrder ,
                              @RequestParam(value = "start" , required = false , defaultValue = "1") Integer start ,
                              @RequestParam(value = "limit" , required = false , defaultValue = "10") Integer limit ,
                              Model model){


       int pageNo = start.intValue() ;
       int totalItems = limit.intValue() ;

        Pageable pageable = PageRequest.of(pageNo - 1 , limit) ;

        Page<Post> postPage=  postService.findFilteredPosts(authorId, tagIds,  isPublished,  startDate,  endDate,  pageable,  sortBy,  sortOrder , search) ;

        List<Post> postList = postPage.getContent() ;
        List<Tag> tagsList = postService.getTagList() ;

        model.addAttribute("currentPage" , pageNo) ;
        model.addAttribute("limit" , totalItems) ;
        model.addAttribute("totalPages" , postPage.getTotalPages()) ;
        model.addAttribute("totalItems" , postPage.getTotalElements()) ;
        model.addAttribute("postList" , postList) ;
     //   model.addAttribute("postt" , new Post()) ;
        model.addAttribute("tagList" , tagsList) ;

        System.out.println(tagIds);
        return "paginatedDashboard" ;
    }

//    @GetMapping("/")
//    public String getAllPosts(Model model){
//
//        return findPaginated(1 , model) ;
//    }

    @GetMapping("/post/{postId}")
    public String getPostById(@PathVariable("postId") Long id , Model model , @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        System.out.println("email is --------------------" + email);
        Post post = postService.getPostById(id);
        model.addAttribute("post" , post);
        //return "post" ;
        return "postwithcomment" ;
    }

    @GetMapping("edit/{postId}")
    public String editPostForm(@PathVariable("postId") Long id , Model model , Authentication authentication){
        String loggedInUser = authentication.getName() ;
        Post post = postService.getPostById(id);

        String author = post.getAuthor().getEmail() ;

        boolean isOwner = author.equals(loggedInUser) ;

        if (!isOwner) {
            throw new AccessDeniedException("You are not allowed to edit this profile.");
        }

        PostToPostRequestDtoMapper mapper = new PostToPostRequestDtoMapper() ;
        PostRequestDto postRequestDto = mapper.mapPostToPostRequestDto(post); ;

//        PostRequestDto postRequestDto = new PostRequestDto() ;
//
//        postRequestDto.setId(post.getId());
//        postRequestDto.setTitle(post.getTitle());
//        postRequestDto.setContent(post.getContent());
//
//        List<Tag> tags = post.getTags() ;
//        String tagString = "" ;
//        for(Tag tag : tags){
//            tagString = tag.getName() + "," ;
//        }
//
//        postRequestDto.setTags(tagString);

        model.addAttribute("post" , postRequestDto);
        return "editpost" ;
    }

    @PostMapping("/editpost")
    public String editPost(@ModelAttribute("post") PostRequestDto postRequestDto){
        postService.updateBlog(postRequestDto);
        return "redirect:/" ;
    }

    @GetMapping("page/{pageNo}")
    public String findPaginated(@PathVariable("pageNo")int pageNumber , Model model){
        int pageSize = 10 ;

        Page<Post> postPage = postService.findAllPaginatedPost(pageNumber , pageSize) ;
        List<Post> postList = postPage.getContent() ;
        List<Tag> tagsList = postService.getTagList() ;

        model.addAttribute("currentPage" , pageNumber) ;
        model.addAttribute("totalPages" , postPage.getTotalPages()) ;
        model.addAttribute("totalItems" , postPage.getTotalElements()) ;
        model.addAttribute("postList" , postList) ;
        model.addAttribute("postt" , new Post()) ;
        model.addAttribute("tagList" , tagsList) ;
        return "paginatedDashboard" ;
    }

    @PostMapping("/comment/{id}")
    public String addComment(@PathVariable("id") Long postId , @RequestParam("userName") String user , @RequestParam("commentText")String commentText){
          commentService.addComment(postId, user, commentText);
          return "redirect:/post/" + postId ;
    }

    @GetMapping("/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long postId){
        postService.deletePost(postId) ;
        return "redirect:/" ;
    }
}
