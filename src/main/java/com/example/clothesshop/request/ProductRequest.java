package com.example.clothesshop.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class ProductRequest {
    private List<MultipartFile> images;
    private String name;
    private String color;
    private Double price;
}
