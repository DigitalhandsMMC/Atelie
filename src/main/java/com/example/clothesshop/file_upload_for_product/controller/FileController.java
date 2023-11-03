package com.example.clothesshop.file_upload_for_product.controller;

import com.example.clothesshop.file_upload_for_product.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

//    @PostMapping("/upload")
//    public ResponseEntity<FileResponse> uploadVideo(@RequestParam("file") MultipartFile file) throws IOException {
//        return fileService.saveVideo(file);
//    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadVideo(@PathVariable Long id) throws FileNotFoundException {
        return fileService.getVideoById(id);
    }

}