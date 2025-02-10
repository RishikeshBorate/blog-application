package com.project.blog_application.repositories;

import com.project.blog_application.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRespository extends JpaRepository<Post , Integer> {

    @Override
    <S extends Post> S save(S entity);

    @Override
    Optional<Post> findById(Integer integer);
}
