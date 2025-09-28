package com.socialmedia.SocialMediaApp.securityService;

import com.socialmedia.SocialMediaApp.entities.Post;
import com.socialmedia.SocialMediaApp.enums.PostVisibility;
import com.socialmedia.SocialMediaApp.exceptions.ResourceNotFoundException;
import com.socialmedia.SocialMediaApp.repository.PostRepository;
import com.socialmedia.SocialMediaApp.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component(value = "postSecurityService")
public class PostSecurityService {

    private PostRepository postRepository;
    private AuthService authService;
    private UserRepository userRepository;

    public PostSecurityService(AuthService authService,UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.authService = authService;
    }

    public boolean canCreatePost(String userId) {
        Optional<com.socialmedia.SocialMediaApp.entities.User> foundUser = authService.getUser();
        if (foundUser.isEmpty()) {
            return false;
        }
        return foundUser.get().getId().equals(userId);
    }

    public boolean canDeletePost(String postId) {
        Optional<com.socialmedia.SocialMediaApp.entities.User> user = authService.getUser();
        if (user.isEmpty()) {
            return false;
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        return post.getUser().getId().equals(user.get().getId());
    }

    public boolean canViewPost(String postId){
        Optional<com.socialmedia.SocialMediaApp.entities.User> authUser = authService.getUser();
        if( authUser.isEmpty() ) {
            return false;
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", postId));
        com.socialmedia.SocialMediaApp.entities.User postUser = post.getUser();
        if(post.getVisibility() == PostVisibility.PUBLIC) return true;
        else if(postUser.getBlockedUsers().contains(authUser)) return false;
        else return postUser.getFollowers().contains(authUser);
    }

}
