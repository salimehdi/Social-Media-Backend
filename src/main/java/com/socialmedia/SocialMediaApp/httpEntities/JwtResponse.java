package com.socialmedia.SocialMediaApp.httpEntities;

import com.socialmedia.SocialMediaApp.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class JwtResponse {

    private boolean success;
    private String token;
    private User user;

}
