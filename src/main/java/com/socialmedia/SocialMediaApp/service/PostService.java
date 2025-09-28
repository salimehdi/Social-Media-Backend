package com.socialmedia.SocialMediaApp.service;

import com.socialmedia.SocialMediaApp.entities.Comment;
import com.socialmedia.SocialMediaApp.exceptions.ResourceNotFoundException;
import com.socialmedia.SocialMediaApp.httpEntities.SuccessResponse;
import com.socialmedia.SocialMediaApp.entities.Post;
import com.socialmedia.SocialMediaApp.entities.User;
import com.socialmedia.SocialMediaApp.repository.CommentRepository;
import com.socialmedia.SocialMediaApp.repository.PostRepository;
import com.socialmedia.SocialMediaApp.repository.UserRepository;
import com.socialmedia.SocialMediaApp.securityService.AuthService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private AuthService authService;
    private CommentRepository commentRepository;

    public PostService(CommentRepository commentRepository,AuthService authService,PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.authService=authService;
        this.commentRepository = commentRepository;
    }


    public Post createPost(Post post,String userId) {
        Optional<User> foundUser = userRepository.findById(userId);
        if(foundUser.isEmpty()){
            throw new ResourceNotFoundException("User", userId);
        }
        User user = foundUser.get();
        post.setUser(user);
        Post savedPost = postRepository.save(post);
        user.getPosts().add(savedPost);
        userRepository.save(user);
        return savedPost;
    }

    public Post getPostById(String postId){
        Optional<Post> foundPost = postRepository.findById(postId);
        if(foundPost.isEmpty()){
            throw new ResourceNotFoundException("Post", postId);
        }
        return foundPost.get();
    }

    public SuccessResponse deletePostById(String postId) {
        Optional<Post> foundPost = postRepository.findById(postId);
        if(foundPost.isEmpty()){
            throw new ResourceNotFoundException("Post", postId);
        }
        User user = foundPost.get().getUser();
        if (user != null) {
            user.getPosts().removeIf(p -> p.getId().equals(postId));
            user.setPostsCount(user.getPosts().size());
            userRepository.save(user);
        }
        postRepository.delete(foundPost.get());
        commentRepository.deleteCommentsByPost(foundPost.get());
        SuccessResponse response = new SuccessResponse();
        response.setSuccess(true);
        response.setMessage("Post deleted successfully");
        return response;
    }

    public String likePost(String postId) {
        String response = "";
        Optional<User> likingUser = authService.getUser();
        Optional<Post> foundPost = postRepository.findById(postId);
        if (foundPost.isEmpty()) {
            throw new ResourceNotFoundException("Post", postId);
        }
        Post post = foundPost.get();
        if (post.getLikedBy().contains(likingUser.get())) {
            post.getLikedBy().remove(likingUser.get());
            post.setLikesCount(post.getLikesCount() - 1);
            response = "Unliked Post Successfully";
        } else {
            post.getLikedBy().add(likingUser.get());
            post.setLikesCount(post.getLikesCount() + 1);
            response = "Liked Post Successfully";
        }
        postRepository.save(post);
        return response;
    }

    public Page<Post> getAllPosts(Pageable pageable) {
         return postRepository.findAll(pageable);
    }

    public List<Comment> getAllComments(String postId) {
        Optional<Post> foundPost = postRepository.findById(postId);
        if (foundPost.isEmpty()) {
            throw new ResourceNotFoundException("Post", postId);
        }
        return foundPost.get().getComments().stream().toList();
    }

}
