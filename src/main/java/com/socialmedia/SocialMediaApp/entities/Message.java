package com.socialmedia.SocialMediaApp.entities;

import com.socialmedia.SocialMediaApp.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

@Document(collection = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    private String id;

    @DBRef
    @Indexed
    private Chat chat;

    @DBRef
    @Indexed
    private User sender;

    private String content;
    private MessageType messageType = MessageType.TEXT;
    private List<String> mediaUrls = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Message status tracking with proper linking
    @DBRef
    private Set<User> deliveredTo = new HashSet<>();

    @DBRef
    private Set<User> readBy = new HashSet<>();

    // Reply functionality
    @DBRef
    private Message replyToMessage;
    private String replyToContent; // Preview of replied message

    // Forward functionality
    @DBRef
    private Chat forwardedFromChat;

    @DBRef
    private Message forwardedFromMessage;
    private boolean isForwarded = false;

    // Message reactions with proper linking
    @DBRef
    private Map<String, Set<User>> reactions = new HashMap<>(); // emoji -> Set of Users

    // Message status
    private boolean isEdited = false;
    private boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private boolean isRecalled = false; // Sender can recall message
}