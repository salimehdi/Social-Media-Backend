package com.socialmedia.SocialMediaApp.entities;

import com.socialmedia.SocialMediaApp.enums.MediaType;
import com.socialmedia.SocialMediaApp.enums.StoryVisibility;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "stories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Story {
    @Id
    private String id;

    @DBRef
    @Indexed
    private User user;

    private String mediaUrl;
    private MediaType mediaType;
    private String caption;
    private String backgroundColor;

    @DBRef
    private List<User> mentions = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @Indexed(expireAfter = "86400")
    private LocalDateTime expiresAt;

    private StoryVisibility visibility = StoryVisibility.PUBLIC;

    @DBRef
    private Set<User> hiddenFrom = new HashSet<>();

    @DBRef
    private Set<User> viewedBy = new HashSet<>();

    @DBRef
    private Set<User> likedBy = new HashSet<>();

    private int viewsCount;
    private int likesCount;

    private boolean isActive = true;
    private boolean isHighlight = false;
}
