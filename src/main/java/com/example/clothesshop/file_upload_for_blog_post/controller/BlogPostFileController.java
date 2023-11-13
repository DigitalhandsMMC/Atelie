package com.example.clothesshop.file_upload_for_blog_post.controller;

import com.example.clothesshop.file_upload_for_blog_post.service.BlogPostFileService;
import com.example.clothesshop.service.BlogPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/blog-post/file")
@RequiredArgsConstructor
public class BlogPostFileController {

    private final BlogPostService blogPostService;
    private final BlogPostFileService blogPostFileService;

//    @PostMapping("/upload")
//    public ResponseEntity<BlogPost> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
//        return fileService.saveVideo(file);
//    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadVideo(@PathVariable Long id) throws FileNotFoundException {
        return blogPostFileService.getVideoById(id);
    }

}