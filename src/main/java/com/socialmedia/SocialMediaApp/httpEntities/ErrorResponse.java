package com.socialmedia.SocialMediaApp.httpEntities;

import lombok.Data;

@Data
public class ErrorResponse {

    private boolean success;
    private String message;
    private String errorCode;

}
