package com.project.blog_application.repositories;

import com.project.blog_application.models.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User , Long> {
    @Override
    <S extends User> S save(S entity);

    Optional<User> findByEmail(String email);

    @Override
    List<User> findAll(Sort sort);
}
