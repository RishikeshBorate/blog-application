package com.project.blog_application.services;

import com.project.blog_application.models.Tag;
import com.project.blog_application.repositories.TagRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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
}
