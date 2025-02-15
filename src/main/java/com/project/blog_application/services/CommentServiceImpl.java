package com.project.blog_application.services;

import com.project.blog_application.models.Comment;
import com.project.blog_application.models.Post;
import com.project.blog_application.repositories.CommentRepository;
import com.project.blog_application.repositories.PostRespository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService{

    private CommentRepository commentRepository ;
    private PostRespository postRespository ;

    public CommentServiceImpl(CommentRepository commentRepository , PostRespository postRespository){
        this.commentRepository = commentRepository ;
        this.postRespository = postRespository ;
    }

    public void addComment(Long postId, String userName, String commentText) {
        Optional<Post> postOptional = postRespository.findById(postId) ;
        Post post = postOptional.get();

        Comment comment = new Comment();
      //  comment.setUserName(userName);
        comment.setCommentText(commentText);
        comment.setPost(post);
        post.getComments().add(commentRepository.save(comment));
        postRespository.save(post);
    }

    @Override
    public Optional<Comment> findCommentById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public void saveSubComment(Long commentId,Long postId , String user, String commentText) {
         Optional<Comment> commentOptional = commentRepository.findById(commentId) ;
         Optional<Post> postOptional = postRespository.findById(postId) ;

         Comment parentComment = commentOptional.get() ;

         Comment subComment = new Comment() ;
         subComment.setCommentText(commentText);
         subComment.setPost(postOptional.get());
         parentComment.getSubComments().add(commentRepository.save(subComment)) ;
         commentRepository.save(parentComment) ;
    }
}