package com.project.blog_application.services;

import com.project.blog_application.dtos.PostRequestDto;
import com.project.blog_application.models.Post;
import com.project.blog_application.models.Tag;
import com.project.blog_application.repositories.CustomPostRepository;
import com.project.blog_application.repositories.PostRespository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Qualifier("PostServiceImpl")
@Service
public class PostServiceImpl implements PostService {
    private PostRespository postRespository ;
    private TagService tagService ;
    private CustomPostRepository customPostRepository ;

    public PostServiceImpl(PostRespository postRespository , TagService tagService , CustomPostRepository customPostRepository){
        this.postRespository = postRespository ;
        this.tagService = tagService ;
        this.customPostRepository = customPostRepository ;
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
//
//        for(String name : tagStringList){
//            Tag tag = new Tag() ;
//
//            tag.setName(name.toLowerCase());
//            tag.setCreatedAt(LocalDateTime.now());
//            tag.setUpdatedAt(LocalDateTime.now());
//
//            Tag savedTag = tagService.save(tag);
//
//            tags.add(savedTag) ;
//        }

        for(String name : tagStringList){
            Optional<Tag> tagOptional = tagService.findByName(name.toLowerCase()) ;

            if(tagOptional.isEmpty()){
                Tag tag = new Tag() ;
                tag.setName(name.toLowerCase());
                tag.setCreatedAt(LocalDateTime.now());
                tag.setUpdatedAt(LocalDateTime.now());
                Tag savedTag = tagService.save(tag);

                tags.add(savedTag) ;
            }else{
                Tag tag = tagOptional.get() ;
                tags.add(tag) ;
            }
        }

        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        post.setPublished(true);

        int size =0 ;
        StringBuilder desc = new StringBuilder() ;
        String content = postRequestDto.getContent() ;

        List<String> contentStringList = Arrays.stream(content.split("\\."))
                .map(String::trim)
                .collect(Collectors.toList());

        for(String sentence : contentStringList){
            if(size > 1 ){
                break;
            }
            desc.append(sentence) ;
            size++ ;
        }

        post.setExcerpt(desc.toString());
        post.setTags(tags);

        postRespository.save(post) ;
    }

    @Override
    public List<Post> getAllPosts(){
        List<Post> postList =  postRespository.findAll() ;
        return postList;
    }

    @Override
    public Post getPostById(Long id){
        Optional<Post> postOptional = postRespository.findById(id) ;
        return postOptional.get() ;
    }

    @Override
    public void updateBlog(PostRequestDto updatedPost){
        Post post = getPostById(updatedPost.getId()) ;

        post.setContent(updatedPost.getContent());
        post.setTitle(updatedPost.getTitle());
        post.setUpdatedAt(LocalDateTime.now());

        String tagsString = updatedPost.getTags();

        System.out.println("tags :" + tagsString);

        List<String> tagStringList = Arrays.stream(tagsString.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        List<Tag> tags = new ArrayList<>() ;

        for(String name : tagStringList){
           Optional<Tag> tagOptional = tagService.findByName(name.toLowerCase()) ;

           if(tagOptional.isEmpty()){
               Tag tag = new Tag() ;
               tag.setName(name.toLowerCase());
               tag.setCreatedAt(LocalDateTime.now());
               tag.setUpdatedAt(LocalDateTime.now());
               Tag savedTag = tagService.save(tag);

               tags.add(savedTag) ;
           }else{
               Tag tag = tagOptional.get() ;
               tags.add(tag) ;
           }
        }

        post.setTags(tags);
        postRespository.save(post) ;
    }

    @Override
    public Page<Post> findAllPaginatedPost(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1 , pageSize) ;
        return postRespository.findAll(pageable);
    }

    @Override
    public List<Tag> getTagList() {
        List<Tag> tagList = tagService.findAllTags();
        return tagList;
    }

    @Override
    public Page<Post> findFilteredPosts(Long authorId, List<Long> tagIds, Boolean isPublished, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable, String sortBy, String sortOrder) {
        return customPostRepository.findFilteredPosts(authorId, tagIds, isPublished, startDate, endDate, pageable, sortBy, sortOrder);
    }


}
