package com.socialmedia.SocialMediaApp.securityService;

import com.socialmedia.SocialMediaApp.entities.Comment;
import com.socialmedia.SocialMediaApp.entities.User;
import com.socialmedia.SocialMediaApp.repository.CommentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "commentSecurityService")
public class CommentSecurityService {

    private AuthService authService;
    private CommentRepository commentRepository;

    public CommentSecurityService(AuthService authService, CommentRepository commentRepository) {
        this.authService = authService;
        this.commentRepository = commentRepository;
    }

    public boolean canDeleteComment(String commentId) {
        Optional<User> user = authService.getUser();
        if (user.isEmpty()) {
            return false;
        }
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));
        return comment.getUser().equals(user.get());
    }

}
