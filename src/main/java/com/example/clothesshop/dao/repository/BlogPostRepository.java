package com.example.clothesshop.dao.repository;

import com.example.clothesshop.dao.entity.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

//    List<BlogPostResponse> getAllBlogPosts();

}
