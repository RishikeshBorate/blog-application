package com.project.blog_application.services;

import com.project.blog_application.dtos.PostRequestDto;
import com.project.blog_application.models.Post;
import com.project.blog_application.models.Tag;
import com.project.blog_application.repositories.PostRespository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Qualifier("PostServiceImpl")
@Service
public class PostServiceImpl implements PostService {
    private PostRespository postRespository ;
    private TagService tagService ;

    public PostServiceImpl(PostRespository postRespository , TagService tagService){
        this.postRespository = postRespository ;
        this.tagService = tagService ;
    }

    @Override
    public void createBlog(PostRequestDto postRequestDto) {
        Post post = new Post() ;

        post.setTitle(postRequestDto.getTitle());
        post.setContent(postRequestDto.getContent());

        String tagString = postRequestDto.getTags() ;

        List<String> tagStringList = Arrays.stream(tagString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        List<Tag> tags = new ArrayList<>() ;

        for(String name : tagStringList){
            Tag tag = new Tag() ;

            tag.setName(name);
            tag.setCreatedAt(LocalDateTime.now());
            tag.setUpdatedAt(LocalDateTime.now());

            Tag savedTag = tagService.save(tag);

            tags.add(savedTag) ;
        }

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setTags(tags);

        postRespository.save(post) ;
    }

    @Override
    public List<Post> getAllPosts(){
        List<Post> postList =  postRespository.findAll() ;
        return postList;
    }
}
