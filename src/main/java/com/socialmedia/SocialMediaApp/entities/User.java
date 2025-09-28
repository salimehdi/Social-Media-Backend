package com.socialmedia.SocialMediaApp.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Document(collection = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    private String id = UUID.randomUUID().toString();

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String firstName;
    private String lastName;
    private String bio;
    private String profilePicture;
    private String coverPicture;

    @CreatedDate
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt = LocalDateTime.now();

    private boolean isPrivate;
    private boolean isVerified;
    private boolean isActive = true;

    @DBRef
    private Set<Post> posts = new HashSet<>();

    @DBRef
    private Set<User> followers = new HashSet<>();

    @DBRef
    private Set<User> following = new HashSet<>();

    @DBRef
    private Set<User> followingRequests = new HashSet<>();

    @DBRef
    private Set<User> blockedUsers = new HashSet<>();

    private int followersCount;
    private int followingCount;
    private int postsCount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}
