package com.example.clothesshop.service;

import com.example.clothesshop.dao.entity.BlogPost;
import com.example.clothesshop.dao.entity.User;
import com.example.clothesshop.dao.repository.BlogPostRepository;
import com.example.clothesshop.dao.repository.UserRepository;
import com.example.clothesshop.enums.UserRole;
import com.example.clothesshop.exception.BlogNotFoundException;
import com.example.clothesshop.exception.error.ErrorMessage;
import com.example.clothesshop.file_upload_for_blog_post.entity.BlogPostFile;
import com.example.clothesshop.file_upload_for_blog_post.repository.BlogPostFileRepository;
import com.example.clothesshop.file_upload_for_blog_post.response.BlogPostFileResponse;
import com.example.clothesshop.file_upload_for_blog_post.service.BlogPostFileService;
import com.example.clothesshop.reponse.BlogPostResponse;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BlogPostService {
    private final BlogPostFileRepository blogPostFileRepository;
    private final BlogPostRepository blogPostRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BlogPostFileService blogPostFileService;

    public BlogPostService(BlogPostFileRepository blogPostFileRepository, BlogPostRepository blogPostRepository, UserRepository userRepository, ModelMapper modelMapper, BlogPostFileService blogPostFileService) {
        this.blogPostFileRepository = blogPostFileRepository;
        this.blogPostRepository = blogPostRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.blogPostFileService = blogPostFileService;
    }

    public ResponseEntity<?> createBlog(MultipartFile image, String title, String content, Long userId) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }

        if (!optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }

        BlogPost blogPost = new BlogPost();
        blogPost.setContent(content);
        blogPost.setTitle(title);
        blogPost.setCreationDate(LocalDateTime.now());
        BlogPost saved = blogPostRepository.save(blogPost);

        BlogPostFileResponse blogPostFileResponse = blogPostFileService.saveVideo(image, blogPost.getId());

        BlogPostResponse blogPostResponse = modelMapper.map(saved, BlogPostResponse.class);
        blogPostResponse.setBlogPostFileId(blogPostFileResponse.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(blogPostResponse);
    }

    public ResponseEntity<?> updateBlog(MultipartFile image, String title, String content, Long blogPostId, Long userId) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }

        if (!optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }

        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new RuntimeException("Blog Post not found"));

        blogPost.setTitle(title);
        blogPost.setContent(content);
        blogPost.setCreationDate(LocalDateTime.now());
        BlogPost saved = blogPostRepository.save(blogPost);

        BlogPostFile oldBlogPostFileByBlogPostId = blogPostFileRepository.findBlogPostFilesByBlogPostId(blogPostId);
        blogPostFileRepository.delete(oldBlogPostFileByBlogPostId);

        if (image != null && !image.isEmpty()) {
            blogPostFileService.saveVideo(image, blogPostId);
        }

        BlogPostFile newBlogPostFilesByBlogPostId = blogPostFileRepository.findBlogPostFilesByBlogPostId(blogPostId);

        BlogPostResponse blogPostResponse = modelMapper.map(saved, BlogPostResponse.class);
        blogPostResponse.setBlogPostFileId(modelMapper.map(newBlogPostFilesByBlogPostId, BlogPostFileResponse.class).getId());

        return ResponseEntity.ok(blogPostResponse);
    }

    public ResponseEntity<List<BlogPostResponse>> getAllBlogs() {
        List<BlogPost> blogPostList = blogPostRepository.findAll();
        List<BlogPostResponse> blogPostResponseList = new ArrayList<>();

        for (BlogPost blogPost : blogPostList) {
            BlogPostFile blogPostFilesByBlogPostId = blogPostFileRepository.findBlogPostFilesByBlogPostId(blogPost.getId());
            BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
            blogPostResponse.setBlogPostFileId(modelMapper.map(blogPostFilesByBlogPostId, BlogPostFileResponse.class).getId());
            blogPostResponseList.add(blogPostResponse);
        }

        return ResponseEntity.ok(blogPostResponseList);
    }

    public ResponseEntity<BlogPostResponse> getBlogPostById(Long blogId) {
        BlogPost blogPost = blogPostRepository.findById(blogId)
                .orElseThrow(() -> new BlogNotFoundException(HttpStatus.NOT_FOUND.name(), ErrorMessage.BLOG_POST_NOT_FOUND));

        BlogPostFile blogPostFilesByBlogPostId = blogPostFileRepository.findBlogPostFilesByBlogPostId(blogId);

        BlogPostResponse blogPostResponse = modelMapper.map(blogPost, BlogPostResponse.class);
        blogPostResponse.setBlogPostFileId(modelMapper.map(blogPostFilesByBlogPostId, BlogPostFileResponse.class).getId());

        return ResponseEntity.status(HttpStatus.OK).body(blogPostResponse);
    }

    public void deleteBlogPost(Long blogPostId) {
        BlogPost blogPost = blogPostRepository.findById(blogPostId)
                .orElseThrow(() -> new BlogNotFoundException(HttpStatus.NOT_FOUND.name(), ErrorMessage.BLOG_POST_NOT_FOUND));
        log.info("Inside deleteBlogPost {}", blogPost);
        blogPostRepository.delete(blogPost);

        BlogPostFile blogPostFilesByBlogPostId = blogPostFileRepository.findBlogPostFilesByBlogPostId(blogPostId);
        blogPostFileRepository.delete(blogPostFilesByBlogPostId);
    }
}
