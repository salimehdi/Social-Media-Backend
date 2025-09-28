package com.socialmedia.SocialMediaApp.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "replies")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Reply {

    private String id;

    @DBRef
    private Comment comment;

    @DBRef
    private User user;
    private String content;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reply)) return false;
        Reply reply = (Reply) o;
        return id != null && id.equals(reply.id);
    }

}
