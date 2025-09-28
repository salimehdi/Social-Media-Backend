package com.socialmedia.SocialMediaApp.controller;

import com.socialmedia.SocialMediaApp.entities.Comment;
import com.socialmedia.SocialMediaApp.httpEntities.PageResponse;
import com.socialmedia.SocialMediaApp.httpEntities.SuccessResponse;
import com.socialmedia.SocialMediaApp.entities.Post;
import com.socialmedia.SocialMediaApp.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostContoller {

    private PostService postService;

    public PostContoller(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/{userId}")
    @PreAuthorize("@postSecurityService.canCreatePost(#userId)")
    public ResponseEntity<Post> createPost(@RequestBody Post post, @PathVariable String userId) {
        Post createdPost = postService.createPost(post,userId);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/{postId}")
    @PreAuthorize("@postSecurityService.canViewPost(#postId)")
    public ResponseEntity<Post> getPostById(@PathVariable String postId) {
        Post foundPost = postService.getPostById(postId);
        return ResponseEntity.ok(foundPost);
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("@postSecurityService.canDeletePost(#postId)")
    public ResponseEntity<SuccessResponse> deletePostById(@PathVariable String postId) {
        postService.deletePostById(postId);
        SuccessResponse response = new SuccessResponse();
        response.setSuccess(true);
        response.setMessage("Post deleted successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("like/{postId}")
    @PreAuthorize("@postSecurityService.canViewPost(#postId)")
    public ResponseEntity<SuccessResponse> likeAPost(@PathVariable String postId) {
        String responseMessage = postService.likePost(postId);
        SuccessResponse response = new SuccessResponse();
        response.setSuccess(true);
        response.setMessage(responseMessage);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public PageResponse getAllPosts(@RequestParam("pageNumber") Integer pageNumber, @RequestParam("pageSize") Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> page = postService.getAllPosts(pageable);
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNumber(page.getNumber());
        pageResponse.setPageSize(page.getSize());
        pageResponse.setTotalPages(page.getTotalPages());
        pageResponse.setData(page.getContent());
        return pageResponse;
    }

    @GetMapping("{postId}/comments")
    @PreAuthorize("@postSecurityService.canViewPost(#postId)")
    public ResponseEntity<List<Comment>> getPostsByComment(@PathVariable String postId) {
        List<Comment> posts = postService.getAllComments(postId);
        return ResponseEntity.ok(posts);
    }
}
