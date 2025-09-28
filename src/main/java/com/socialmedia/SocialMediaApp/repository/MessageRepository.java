package com.socialmedia.SocialMediaApp.repository;

import com.socialmedia.SocialMediaApp.entities.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
}
