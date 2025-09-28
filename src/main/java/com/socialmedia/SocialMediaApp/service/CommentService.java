package com.socialmedia.SocialMediaApp.service;

import com.socialmedia.SocialMediaApp.entities.Comment;
import com.socialmedia.SocialMediaApp.entities.Post;
import com.socialmedia.SocialMediaApp.entities.User;
import com.socialmedia.SocialMediaApp.exceptions.ResourceNotFoundException;
import com.socialmedia.SocialMediaApp.repository.CommentRepository;
import com.socialmedia.SocialMediaApp.repository.PostRepository;
import com.socialmedia.SocialMediaApp.repository.ReplyRepository;
import com.socialmedia.SocialMediaApp.securityService.AuthService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private AuthService authService;
    private ReplyRepository replyRepository;

    public CommentService(AuthService authService,CommentRepository commentRepository, PostRepository postRepository) {
        this.postRepository = postRepository;
        this.authService = authService;
        this.commentRepository = commentRepository;
    }

    public Comment createComment(Comment comment, String postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        comment.setPost(post);
        Optional<User> authUser = authService.getUser();
        comment.setUser(authUser.get());
        Comment savedComment = commentRepository.save(comment);
        post.getComments().add(comment);
        postRepository.save(post);
        return savedComment;
    }

    public String deleteCommentById(String commentId) {
        Optional<Comment> foundComment = commentRepository.findById(commentId);
        if (foundComment.isEmpty()) {
            throw new ResourceNotFoundException("Comment", commentId);
        }
        Comment comment = foundComment.get();
        Post post = comment.getPost();
        post.getComments().removeIf(c -> c.getId().equals(commentId));
        replyRepository.deleteRepliesByComment(comment);
        postRepository.save(post);
        commentRepository.delete(comment);
        return "Comment deleted successfully";
    }

}
