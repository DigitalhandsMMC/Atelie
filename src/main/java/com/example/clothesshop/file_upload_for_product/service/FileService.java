package com.example.clothesshop.file_upload_for_product.service;

import com.example.clothesshop.file_upload_for_product.entity.File;
import com.example.clothesshop.file_upload_for_product.exception.FileNotFoundException;
import com.example.clothesshop.file_upload_for_product.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;


    public File saveVideo(MultipartFile multipartFile, Long productId) throws IOException {
        File file = new File();
        file.setName(multipartFile.getOriginalFilename());
        file.setContentType(multipartFile.getContentType());
        file.setFileData(multipartFile.getBytes());
        file.setProductId(productId);
        File saved = fileRepository.save(file);

//        FileResponse frommedModelToResponse = fileMapper.fromModelToResponse(saved);
        return saved;
    }

    public ResponseEntity<?> getVideoById(Long id) throws FileNotFoundException {
        File fileOptional = fileRepository.findById(id).orElseThrow(
                () -> new FileNotFoundException("sd"));
        return ResponseEntity.status(HttpStatus.OK)
                .header("Content-Type", fileOptional.getContentType())
                .body(fileOptional.getFileData());
    }

}