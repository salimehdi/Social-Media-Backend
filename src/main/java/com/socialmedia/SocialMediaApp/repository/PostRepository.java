package com.socialmedia.SocialMediaApp.repository;

import com.socialmedia.SocialMediaApp.entities.Post;
import com.socialmedia.SocialMediaApp.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findAllByUser(User user);

    void deleteAllByUser(User user);

}
