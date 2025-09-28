package com.socialmedia.SocialMediaApp.repository;

import com.socialmedia.SocialMediaApp.entities.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRepository extends MongoRepository<Chat, String> {
}
