package com.example.clothesshop.file_upload_for_blog_post.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogPostFileResponse {

    private Long id;

    private String name;
    private String contentType;
    private byte[] fileData;


}