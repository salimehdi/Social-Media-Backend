package com.socialmedia.SocialMediaApp.repository;

import com.socialmedia.SocialMediaApp.entities.Comment;
import com.socialmedia.SocialMediaApp.entities.Post;
import com.socialmedia.SocialMediaApp.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
    void deleteCommentsByPost(Post post);
    void deleteCommentsByUser(User user);
}
