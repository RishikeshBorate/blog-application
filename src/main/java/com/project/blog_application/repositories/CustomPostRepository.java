package com.project.blog_application.repositories;

import com.project.blog_application.models.Post;
import com.project.blog_application.models.Tag;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomPostRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public Page<Post> findFilteredPosts(Long authorId, List<Long> tagIds, Boolean isPublished,
                                        LocalDateTime startDate, LocalDateTime endDate, Pageable pageable, String sortBy, String sortDirection) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Post> query = cb.createQuery(Post.class);
        Root<Post> root = query.from(Post.class);

        List<Predicate> predicates = new ArrayList<>();

        if (tagIds != null && !tagIds.isEmpty()) {
            Join<Post, Tag> tagsJoin = root.join("tags", JoinType.INNER);
            predicates.add(tagsJoin.get("id").in(tagIds));
        }

        //        if (search != null && !search.trim().isEmpty()) {
//            String likePattern = "%" + search.toLowerCase() + "%";
//            Predicate titlePredicate = cb.like(cb.lower(root.get("title")), likePattern);
//            Predicate contentPredicate = cb.like(cb.lower(root.get("content")), likePattern);
//            Predicate publisherPredicate = cb.like(cb.lower(root.get("publisher")), likePattern);
//
//            Join<Post, Tag> tagsJoin = root.join("tags", JoinType.LEFT);
//            Predicate tagsPredicate = cb.like(cb.lower(tagsJoin.get("name")), likePattern);
//
//            predicates.add(cb.or(titlePredicate, contentPredicate, publisherPredicate, tagsPredicate));
//        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));

        if (sortBy != null) {
            Path<?> sortField = root.get(sortBy);
            if ("desc".equalsIgnoreCase(sortDirection)) {
                query.orderBy(cb.desc(sortField));
            } else {
                query.orderBy(cb.asc(sortField));
            }
        }

        TypedQuery<Post> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Post> countRoot = countQuery.from(Post.class);
        countQuery.select(cb.count(countRoot));

        List<Predicate> countPredicates = new ArrayList<>();
        if (authorId != null) {
            countPredicates.add(cb.equal(countRoot.get("author").get("id"), authorId));
        }
        if (isPublished != null) {
            countPredicates.add(cb.equal(countRoot.get("isPublished"), isPublished));
        }

        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long totalCount = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(typedQuery.getResultList(), pageable, totalCount);
    }
}
