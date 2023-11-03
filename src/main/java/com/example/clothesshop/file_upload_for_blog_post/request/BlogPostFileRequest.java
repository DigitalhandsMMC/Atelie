package com.example.clothesshop.file_upload_for_blog_post.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlogPostFileRequest {

    private String name;
    private String contentType;
    private byte[] fileData;

}