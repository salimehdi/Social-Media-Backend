package com.socialmedia.SocialMediaApp.entities;

import com.socialmedia.SocialMediaApp.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.Message;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Document(collection = "chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    private String id;

    // Participants in the chat with proper linking
    @DBRef
    @Indexed
    private Set<User> participants = new HashSet<>();

    private ChatType chatType; // DIRECT or GROUP
    private String chatName; // For group chats
    private String chatImage; // For group chats
    private String description; // For group chats

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Group chat specific fields
    @DBRef
    private User createdBy; // User who created the group

    @DBRef
    private Set<User> admins = new HashSet<>(); // For group chats

    // Last message info for quick display
    @DBRef
    private Message lastMessage;
    private LocalDateTime lastMessageAt;

    // Chat settings
    private boolean isActive = true;
    private boolean isArchived = false;

    @DBRef
    private Map<User, Boolean> mutedBy = new HashMap<>(); // User -> isMuted

    @DBRef
    private Map<User, LocalDateTime> lastSeenBy = new HashMap<>(); // User -> lastSeenTime

    // Message counts
    private int totalMessagesCount;

    @DBRef
    private Map<User, Integer> unreadCountByUser = new HashMap<>();
}