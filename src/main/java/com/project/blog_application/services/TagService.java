package com.project.blog_application.services;

import com.project.blog_application.models.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
     Tag save(Tag tag);

     Optional<Tag> findByName(String tagName) ;

     List<Tag> findAllTags() ;
}
