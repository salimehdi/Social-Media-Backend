package com.socialmedia.SocialMediaApp.entities;

import com.socialmedia.SocialMediaApp.enums.PostVisibility;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    @Id
    private String id;

    private String content;
    private List<String> mediaUrls = new ArrayList<>();
    private String location;
    private List<String> tags = new ArrayList<>();

    @DBRef
    private List<User> mentions = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private PostVisibility visibility = PostVisibility.PUBLIC;

    @DBRef
    private Set<User> likedBy = new HashSet<>();

    @DBRef
    private Set<User> savedBy = new HashSet<>();

    @DBRef
    private Set<User> sharedBy = new HashSet<>();

    @DBRef
    private Set<Comment> comments = new HashSet<>();

    private int likesCount;
    private int commentsCount;
    private int sharesCount;
    private int savesCount;

    private boolean isActive = true;
    private boolean isReported = false;

    @DBRef
    @Indexed
    private User user;
}
