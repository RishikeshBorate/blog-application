package com.project.blog_application.repositories;

import com.project.blog_application.models.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRespository extends JpaRepository<Post , Long> {

    @Override
    <S extends Post> S save(S entity);

    @Override
    Optional<Post> findById(Long aLong);

    @Override
    List<Post> findAll(Sort sort);
}
