package com.socialmedia.SocialMediaApp.exceptions;

import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException {

    private String resourceName;
    private String fieldName;

    public ResourceNotFoundException(String resourceName, String fieldName) {
        super("Resource not found: " + resourceName + " with field: " + fieldName);
        this.resourceName = resourceName;
        this.fieldName = fieldName;
    }
}
