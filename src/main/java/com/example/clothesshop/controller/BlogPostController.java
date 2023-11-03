package com.example.clothesshop.controller;

import com.example.clothesshop.dao.entity.User;
import com.example.clothesshop.dao.repository.UserRepository;
import com.example.clothesshop.enums.UserRole;
import com.example.clothesshop.reponse.BlogPostResponse;
import com.example.clothesshop.service.BlogPostService;
import com.example.clothesshop.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/blogs")
public class BlogPostController {


    private BlogPostService blogService;
    private UserRepository userRepository;

    public BlogPostController(BlogPostService blogService, UserService userService, UserRepository userRepository) {
        this.blogService = blogService;
        this.userRepository = userRepository;
    }

    @PostMapping("createBlog/{userId}")
    public ResponseEntity<?> createBlog(@RequestParam(name = "image", required = false) MultipartFile image,
                                        @RequestParam("title") String title, @RequestParam("content") String content,
                                        @PathVariable(name = "userId") Long userId) throws IOException {
        return blogService.createBlog(image, title, content, userId);
    }

    @PutMapping("{blogPostId}/updateBlog/{userId}")
    public ResponseEntity<?> updateBlog(@RequestParam(name = "image", required = false) MultipartFile image,
                                        @RequestParam("title") String title, @RequestParam("content") String content,
                                        @PathVariable(name = "blogPostId") Long blogPostId, @PathVariable(name = "userId") Long userId) throws IOException {
        return blogService.updateBlog(image, title, content, blogPostId, userId);
    }

    @GetMapping("/getAllBlogs")
    public ResponseEntity<List<BlogPostResponse>> getAllBlogs() {
        return blogService.getAllBlogs();

    }

    @GetMapping("getBlogById/{blogId}")
    public ResponseEntity<BlogPostResponse> getBlogById(@PathVariable Long blogId) {
        return blogService.getBlogPostById(blogId);
    }

    @DeleteMapping("/{userId}/deleteBlogPost/{blogPostId}")
    public ResponseEntity<?> deleteBlogPost(@PathVariable Long userId, @PathVariable Long blogPostId) {

        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }

        if (!optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }

        blogService.deleteBlogPost(blogPostId);
        return ResponseEntity.noContent().build();
    }
}


