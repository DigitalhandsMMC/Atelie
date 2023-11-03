package com.example.clothesshop.file_upload_for_product.repository;

import com.example.clothesshop.file_upload_for_product.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findByProductId(Long productId);

}