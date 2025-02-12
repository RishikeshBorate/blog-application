package com.project.blog_application.dtos;

import com.project.blog_application.models.Post;
import com.project.blog_application.models.Tag;

import java.util.List;

public class PostToPostRequestDtoMapper {

    public PostRequestDto mapPostToPostRequestDto(Post post){
        PostRequestDto postRequestDto = new PostRequestDto();

        postRequestDto.setId(post.getId());
        postRequestDto.setTitle(post.getTitle());
        postRequestDto.setContent(post.getContent());
        postRequestDto.setAuthor(post.getAuthor());
        postRequestDto.setExcerpt(post.getExcerpt());
        postRequestDto.setPublisher(post.getPublisher());
        postRequestDto.setPublished(post.isPublished());
        postRequestDto.setCreatedAt(post.getCreatedAt());
        postRequestDto.setUpdatedAt(post.getUpdatedAt());

        List<Tag> tagList = post.getTags() ;
        StringBuilder tagString = new StringBuilder();

        for(Tag tag : tagList){
            tagString.append(tag.getName()) ;
            tagString.append(",") ;
        }

        postRequestDto.setTags(tagString.toString());

        return  postRequestDto ;
    }
}
