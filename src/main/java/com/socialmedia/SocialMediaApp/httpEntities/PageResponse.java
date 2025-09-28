package com.socialmedia.SocialMediaApp.httpEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse {

    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private Object data;

}
