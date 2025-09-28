package com.socialmedia.SocialMediaApp.controller;
import com.socialmedia.SocialMediaApp.entities.Post;
import com.socialmedia.SocialMediaApp.httpEntities.ErrorResponse;
import com.socialmedia.SocialMediaApp.httpEntities.SuccessResponse;
import com.socialmedia.SocialMediaApp.entities.User;
import com.socialmedia.SocialMediaApp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("@userSecurityService.canViewProfile(#userId)")
    public ResponseEntity getUserById(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            HashMap<String,String> response = new HashMap<>();
            response.put("message", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@userSecurityService.canDeleteUser(#userId)")
    public ResponseEntity<SuccessResponse> deleteUserById(@PathVariable String userId) {
        SuccessResponse response = userService.deleteUserById(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("@userSecurityService.canUpdateUser(#userId)")
    public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User user) {
        User updatedUser = userService.updateUser(userId, user);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/block/{userIdToBlock}")
    @PreAuthorize("@userSecurityService.restrictUserItself(#userIdToBlock)")
    public ResponseEntity<SuccessResponse> blockUser(@PathVariable String userIdToBlock) {
        userService.blockUser(userIdToBlock);
        SuccessResponse response = new SuccessResponse();
        response.setSuccess(true);
        response.setMessage("User with userId "+userIdToBlock+" blocked successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/follow/{userIdToFollow}")
    @PreAuthorize("@userSecurityService.restrictUserItself(#userIdToFollow)")
    public ResponseEntity<SuccessResponse> followUser(@PathVariable String userIdToFollow) {
        String responseMessage = userService.followUser(userIdToFollow);
        SuccessResponse response = new SuccessResponse();
        response.setSuccess(true);
        response.setMessage(responseMessage);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/unfollow/{userIdToFollow}")
    @PreAuthorize("@userSecurityService.restrictUserItself(#userIdToFollow)")
    public ResponseEntity<SuccessResponse> unFollowUser(@PathVariable String userIdToFollow) {
        userService.unFollowUser(userIdToFollow);
        SuccessResponse response = new SuccessResponse();
        response.setSuccess(true);
        response.setMessage("Sucessfully unfollowed User with userId "+userIdToFollow);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accept/{userIdToFollow}")
    @PreAuthorize("@userSecurityService.restrictUserItself(#userIdToFollow)")
    public ResponseEntity<SuccessResponse> acceptFollowRequest(@PathVariable String userIdToFollow) {
        userService.acceptFollowRequest(userIdToFollow);
        SuccessResponse response = new SuccessResponse();
        response.setSuccess(true);
        response.setMessage("Accepted Follow Request from User with userId "+ userIdToFollow);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getrequests")
    public ResponseEntity<Set<User>> getFollowRequests() {
        Set<User> followRequests = userService.getFollowRequests();
        return ResponseEntity.ok(followRequests);
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity getAllUserPosts(@PathVariable String userId) {
        List<Post> posts = userService.getAllUserPosts(userId);
        return ResponseEntity.ok(posts);
    }

}