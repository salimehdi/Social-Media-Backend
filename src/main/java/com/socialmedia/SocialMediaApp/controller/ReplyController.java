package com.socialmedia.SocialMediaApp.controller;

import com.socialmedia.SocialMediaApp.entities.Reply;
import com.socialmedia.SocialMediaApp.httpEntities.SuccessResponse;
import com.socialmedia.SocialMediaApp.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/comments")
public class ReplyController {

    private ReplyService replyService;

    public ReplyController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("/{commentId}/reply")
    public ResponseEntity<Reply> replyToComment(@RequestBody Reply reply,@PathVariable String commentId) {
        Reply savedReply = replyService.replyToComment(reply, commentId);
        return ResponseEntity.ok(savedReply);
    }

    @DeleteMapping("/reply/{replyId}")
    public ResponseEntity<SuccessResponse> deleteReplyById(@PathVariable String replyId) {
        String responseMessage = replyService.deleteReplyById(replyId);
        SuccessResponse successResponse = new SuccessResponse();
        successResponse.setSuccess(true);
        successResponse.setMessage(responseMessage);
        return ResponseEntity.ok(successResponse);
    }

}
