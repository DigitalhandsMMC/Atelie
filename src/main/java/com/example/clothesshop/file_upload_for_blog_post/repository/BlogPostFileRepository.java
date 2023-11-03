package com.example.clothesshop.file_upload_for_blog_post.repository;


import com.example.clothesshop.dao.entity.BlogPost;
import com.example.clothesshop.file_upload_for_blog_post.entity.BlogPostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogPostFileRepository extends JpaRepository<BlogPostFile, Long> {

    BlogPostFile findBlogPostFilesByBlogPostId(Long blogPostId);

    void delete(BlogPostFile file);
}