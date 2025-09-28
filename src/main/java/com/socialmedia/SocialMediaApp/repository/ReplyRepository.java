package com.socialmedia.SocialMediaApp.repository;

import com.socialmedia.SocialMediaApp.entities.Comment;
import com.socialmedia.SocialMediaApp.entities.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReplyRepository extends MongoRepository<Reply, String> {
    void deleteRepliesByComment(Comment comment);
}
