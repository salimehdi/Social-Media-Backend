package com.socialmedia.SocialMediaApp.service;

import com.socialmedia.SocialMediaApp.entities.Post;
import com.socialmedia.SocialMediaApp.exceptions.ResourceNotFoundException;
import com.socialmedia.SocialMediaApp.httpEntities.SuccessResponse;
import com.socialmedia.SocialMediaApp.entities.User;
import com.socialmedia.SocialMediaApp.repository.CommentRepository;
import com.socialmedia.SocialMediaApp.repository.PostRepository;
import com.socialmedia.SocialMediaApp.repository.UserRepository;
import com.socialmedia.SocialMediaApp.securityService.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private PasswordEncoder passwordEncoder;
    private AuthService authService;
    private UserRepository userRepository;
    private PostRepository postRepository;
    private CommentRepository commentRepository;

    UserService(CommentRepository commentRepository,PostRepository postRepository,UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.passwordEncoder = passwordEncoder;
        this.postRepository = postRepository;
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getUserById(String userId){
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isEmpty()){
            throw new ResourceNotFoundException("User", userId);
        }
        return foundUser.get();
    }

    public SuccessResponse deleteUserById(String userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if (foundUser.isEmpty()) {
            throw new ResourceNotFoundException("User", userId);
        }
        userRepository.deleteById(userId);
        postRepository.deleteAllByUser(foundUser.get());
        commentRepository.deleteCommentsByUser(foundUser.get());
        SuccessResponse response = new SuccessResponse();
        response.setSuccess(true);
        response.setMessage("User deleted successfully");
        return response;
     }

     public User updateUser(String userId, User userDetails) {
         Optional<User> foundUser = userRepository.findById(userId);
         if (foundUser.isEmpty()) {
             throw new ResourceNotFoundException("User", userId);
         }
         User existingUser = foundUser.get();
         existingUser.setUsername(userDetails.getUsername());
         existingUser.setEmail(userDetails.getEmail());
         existingUser.setFirstName(userDetails.getFirstName());
         existingUser.setLastName(userDetails.getLastName());
         existingUser.setBio(userDetails.getBio());
         existingUser.setProfilePicture(userDetails.getProfilePicture());
         existingUser.setCoverPicture(userDetails.getCoverPicture());
         existingUser.setPrivate(userDetails.isPrivate());
         existingUser.setVerified(userDetails.isVerified());
         return userRepository.save(existingUser);
     }

     public boolean existsByUsername(String username) {
         return userRepository.existsByUsername(username);
     }

     public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", username));
    }

    public void blockUser(String blockedUserId){
        User user = authService.getUser().orElseThrow(() -> new ResourceNotFoundException("User", "not authenticated"));
        User blockedUser = userRepository.findById(blockedUserId).orElseThrow(()-> new ResourceNotFoundException("User", blockedUserId));
        user.getBlockedUsers().add(blockedUser);
        userRepository.save(user);
    }

    public void unFollowUser(String followeeUserId) {
        User user = authService.getUser().orElseThrow(() -> new ResourceNotFoundException("User", "notAuthenticated"));
        User followeeUser = userRepository.findById(followeeUserId).orElseThrow(() -> new ResourceNotFoundException("User", followeeUserId));
        user.getFollowing().remove(followeeUser);
        user.setFollowingCount(user.getFollowing().size());
        followeeUser.getFollowers().remove(user);
        followeeUser.setFollowersCount(followeeUser.getFollowers().size());
        userRepository.save(user);
        userRepository.save(followeeUser);
    }

    public void acceptFollowRequest(String followerUserId) {
        User user = authService.getUser().orElseThrow(() -> new ResourceNotFoundException("User", "notAuthenticated"));
        User followerUser = userRepository.findById(followerUserId).orElseThrow(() -> new ResourceNotFoundException("User", followerUserId));
        user.getFollowers().add(followerUser);
        user.getFollowingRequests().remove(followerUser);
        user.setFollowersCount(user.getFollowers().size());
        followerUser.getFollowing().add(user);
        followerUser.setFollowingCount(followerUser.getFollowing().size());
        userRepository.save(user);
        userRepository.save(followerUser);
    }


    public String followUser(String followeeUserId){
        User user = authService.getUser().orElseThrow(() -> new ResourceNotFoundException("User", "not authenticated"));
        User followeeUser = userRepository.findById(followeeUserId).orElseThrow(()-> new ResourceNotFoundException("User", followeeUserId));
        if(followeeUser.isPrivate()){
            followeeUser.getFollowingRequests().add(user);
            userRepository.save(followeeUser);
            return "Follow request sent to User with userId " +followeeUserId;
        }
        followeeUser.getFollowers().add(user);
        user.getFollowing().add(followeeUser);
        userRepository.save(user);
        userRepository.save(followeeUser);
        return "Successfully followed User with user Id " + followeeUserId;
    }

    public Set<User> getFollowRequests() {
        User user = authService.getUser().orElseThrow(() -> new ResourceNotFoundException("User", "not authenticated"));
        return user.getFollowingRequests();
    }

    public List<Post> getAllUserPosts(String userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", userId));
        return postRepository.findAllByUser(user);
    }
}
