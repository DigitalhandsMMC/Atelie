package com.example.clothesshop.file_upload_for_blog_post.service;

import com.example.clothesshop.file_upload_for_blog_post.entity.BlogPostFile;
import com.example.clothesshop.file_upload_for_blog_post.repository.BlogPostFileRepository;
import com.example.clothesshop.file_upload_for_blog_post.response.BlogPostFileResponse;
import com.example.clothesshop.file_upload_for_product.entity.File;
import com.example.clothesshop.file_upload_for_product.response.FileResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class BlogPostFileService {

    private final BlogPostFileRepository blogPostFileRepository;
    private final ModelMapper modelMapper;
//    private final FileMapper fileMapper;

    public BlogPostFileResponse saveVideo(MultipartFile multipartFile, Long blogPostId) throws IOException {
        BlogPostFile file = new BlogPostFile();
        file.setName(multipartFile.getOriginalFilename());
        file.setContentType(multipartFile.getContentType());
        file.setFileData(multipartFile.getBytes());
        file.setBlogPostId(blogPostId);
        BlogPostFile saved = blogPostFileRepository.save(file);
        BlogPostFileResponse BlogPostFileResponse = modelMapper.map(saved, BlogPostFileResponse.class);
        return BlogPostFileResponse;
    }

    public ResponseEntity<?> getVideoById(Long id) throws FileNotFoundException {
        BlogPostFile fileOptional = blogPostFileRepository.findById(id).orElseThrow(
                () -> new FileNotFoundException("File not found"));
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", fileOptional.getContentType())
                .body(fileOptional.getFileData());
    }

}