package com.socialmedia.SocialMediaApp.service;

import com.socialmedia.SocialMediaApp.entities.Comment;
import com.socialmedia.SocialMediaApp.entities.Reply;
import com.socialmedia.SocialMediaApp.repository.CommentRepository;
import com.socialmedia.SocialMediaApp.repository.ReplyRepository;
import com.socialmedia.SocialMediaApp.securityService.AuthService;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {

    private ReplyRepository replyRepository;
    private CommentRepository commentRepository;
    private AuthService authService;

    public ReplyService(ReplyRepository replyRepository, CommentRepository commentRepository, AuthService authService) {
        this.replyRepository = replyRepository;
        this.authService = authService;
        this.commentRepository = commentRepository;
    }

    public Reply replyToComment(Reply reply,String commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found with id: " + commentId));
        reply.setUser(authService.getUser().orElseThrow(() -> new RuntimeException("User not authenticated")));
        reply.setComment(comment);
        return replyRepository.save(reply);
    }

    public String deleteReplyById(String replyId) {
        Reply reply = replyRepository.findById(replyId)
                .orElseThrow(() -> new RuntimeException("Reply not found with id: " + replyId));
        if (!authService.getUser().orElseThrow(() -> new RuntimeException("User not authenticated")).getId().equals(reply.getUser().getId())) {
            throw new RuntimeException("You do not have permission to delete this reply");
        }
        replyRepository.delete(reply);
        return "Reply deleted successfully";
    }

}
