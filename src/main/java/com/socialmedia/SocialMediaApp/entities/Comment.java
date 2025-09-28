package com.socialmedia.SocialMediaApp.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    private String id;

    @DBRef
    @Indexed
    private User user;

    @DBRef
    @Indexed
    private Post post;

    private String content;

    @DBRef
    private List<Reply> replies = new ArrayList<>();

    @DBRef
    private List<User> mentions = new ArrayList<>();

    private LocalDateTime createdAt= LocalDateTime.now();

    private LocalDateTime updatedAt= LocalDateTime.now();

    @DBRef
    private Set<User> likedBy = new HashSet<>();

    private int likesCount;
    private int repliesCount;

    private boolean isActive = true;
    private boolean isEdited = false;
    private boolean isReported = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return id != null && id.equals(comment.id);
    }
}
