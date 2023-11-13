package com.example.clothesshop.reponse;

import com.example.clothesshop.file_upload_for_blog_post.response.BlogPostFileResponse;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class BlogPostResponse {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime creationDate;
    private Long blogPostFileId;
}
