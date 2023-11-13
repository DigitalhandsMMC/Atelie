package com.example.clothesshop.file_upload_for_blog_post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blog_post_file")
public class BlogPostFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String contentType;

    @Column(columnDefinition = "LONG VARBINARY")
    private byte[] fileData;

    @Column(name = "blog_post_id")
    private Long blogPostId;

}