package com.project.blog_application.repositories;

import com.project.blog_application.models.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag , Long> {
    @Override
    <S extends Tag> S save(S entity);

    Optional<Tag> findByName(String name);

    @Override
    List<Tag> findAll(Sort sort);
}
