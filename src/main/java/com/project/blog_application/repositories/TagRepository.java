package com.project.blog_application.repositories;

import com.project.blog_application.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag , Long> {
    @Override
    <S extends Tag> S save(S entity);
}
