package com.project.blog_application.services;

import com.project.blog_application.models.Tag;
import com.project.blog_application.repositories.TagRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Primary
public class TagServiceImpl implements TagService{
    private TagRepository tagRepository ;

    public TagServiceImpl(TagRepository tagRepository){
        this.tagRepository = tagRepository ;
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag) ;
    }

    @Override
    public Optional<Tag> findByName(String tagName){
        Optional<Tag> tagOptional =  tagRepository.findByName(tagName) ;
        return tagOptional ;
    }

    @Override
    public List<Tag> findAllTags() {
        return tagRepository.findAll() ;
    }
}
