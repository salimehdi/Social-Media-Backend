package com.socialmedia.SocialMediaApp.controller;

import com.socialmedia.SocialMediaApp.entities.Comment;
import com.socialmedia.SocialMediaApp.httpEntities.SuccessResponse;
import com.socialmedia.SocialMediaApp.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController("/posts")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{postId}/comments")
    public Comment createComment(@RequestBody Comment comment, @PathVariable String postId) {
        return commentService.createComment(comment, postId);
    }

    @DeleteMapping("/comments/{commentId}")
    @PreAuthorize("@commentSecurityService.canDeleteComment(#commentId)")
    public ResponseEntity<SuccessResponse> deleteCommentById(@PathVariable String commentId) {
        String responseMessage = commentService.deleteCommentById(commentId);
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setSuccess(true);
        successResponse.setMessage(responseMessage);
        return ResponseEntity.ok(successResponse);
    }

}
