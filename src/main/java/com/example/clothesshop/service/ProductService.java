package com.example.clothesshop.service;

import com.example.clothesshop.dao.entity.Product;
import com.example.clothesshop.dao.entity.User;
import com.example.clothesshop.dao.repository.ProductRepository;
import com.example.clothesshop.dao.repository.UserRepository;
import com.example.clothesshop.enums.*;
import com.example.clothesshop.file_upload_for_product.entity.File;
import com.example.clothesshop.file_upload_for_product.repository.FileRepository;
import com.example.clothesshop.file_upload_for_product.response.FileResponse;
import com.example.clothesshop.file_upload_for_product.service.FileService;
import com.example.clothesshop.reponse.ProductResponse;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private  final FileService fileService;
    private final ModelMapper modelMapper;
    private final FileRepository fileRepository;
    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, FileService fileService, ModelMapper modelMapper, FileRepository fileRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ResponseEntity<?> saveProduct(List<MultipartFile> images, String name, String color, Double price, Long userId, String size, Style style, Length length, PropertyType propertyType) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }
        if (!optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setColor(color);
        product.setSize(size);
        product.setStyle(style);
        product.setLength(length);
        product.setPropertyType(propertyType);
        Product savedProduct = productRepository.save(product);
        if (images != null && !images.isEmpty()) {
            for (int i = 1; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                fileService.saveVideo(image, savedProduct.getId());
            }
        }
        List<File> fileList = fileRepository.findByProductId(savedProduct.getId());
        List<FileResponse> fileResponseList = fileList.stream()
                .map(file -> modelMapper.map(file, FileResponse.class)).toList();

        ProductResponse mappedProduct = modelMapper.map(savedProduct, ProductResponse.class);
        mappedProduct.setFileResponseList(fileResponseList);

        return ResponseEntity.status(HttpStatus.CREATED).body(mappedProduct);
    }

    public ResponseEntity<?> getAllProducts(Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }

        if (!optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }
        List<Product> productList = productRepository.findAll();
        List<ProductResponse>  productResponseList= new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
            List<File> fileList = fileRepository.findByProductId(product.getId());
            List<FileResponse> fileResponses = fileList.stream().map(file -> modelMapper.map(file, FileResponse.class)).toList();
            productResponse.setFileResponseList(fileResponses);
            productResponseList.add(productResponse);
        }
        return ResponseEntity.ok(productResponseList);
    }

    public ResponseEntity<?> getProductById(Long productId, Long userId){
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }
        if (!optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }
        Product product = productRepository.findById(productId).get();
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        List<File> fileList = fileRepository.findByProductId(product.getId());
        List<FileResponse> fileResponseList = fileList.stream().map(file -> modelMapper.map(file, FileResponse.class)).toList();
        productResponse.setFileResponseList(fileResponseList);
        return ResponseEntity.ok(productResponse);
    }

    @Transactional
    public ResponseEntity<?> updateProduct(List<MultipartFile> images, String name, String color, Double price, Long productId, Long userId, String size, Style style, Length length, PropertyType propertyType) throws IOException {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }
        if (!optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }
        if (productId == null || !productRepository.existsById(productId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        product.setName(name);
        product.setColor(color);
        product.setPrice(price);
        product.setSize(size);
        product.setStyle(style);
        product.setLength(length);
        product.setPropertyType(propertyType);
        Product savedProduct = productRepository.save(product);
        List<File> oldFiles = fileRepository.findByProductId(productId);
        fileRepository.deleteAll(oldFiles);
        if (images != null && !images.isEmpty()) {
            for (int i = 1; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                fileService.saveVideo(image, productId);
            }
        }
        List<File> newFiles = fileRepository.findByProductId(productId);
        List<FileResponse> fileResponses = newFiles.stream()
                .map(file -> modelMapper.map(file , FileResponse.class)).toList();
        ProductResponse productResponse = modelMapper.map(savedProduct, ProductResponse.class);
        productResponse.setFileResponseList(fileResponses);
        return ResponseEntity.status(HttpStatus.CREATED).body(productResponse);
    }

    public void deleteProductById(Long productId){

        productRepository.deleteById(productId);
    }

    public ResponseEntity<?> filterProducts(Style style, Size size, Length length, PropertyType propertyType, Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }
        if (!optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }
        List<Product> productList;
        if (style != null && size != null && length != null && propertyType != null) {
            productList = productRepository.findByStyleAndSizeAndLengthAndPropertyType(style, size, length, propertyType);
        } else if (style != null && size != null && length != null) {
            productList = productRepository.findByStyleAndSizeAndLength(style, size, length);
        } else if (style != null && size != null) {
            productList = productRepository.findByStyleAndSize(style, size);
        } else if (style != null && length != null) {
            productList = productRepository.findByStyleAndLength(style, length);
        } else if (style != null && propertyType != null) {
            productList = productRepository.findByStyleAndPropertyType(style, propertyType);
        } else if (size != null && length != null) {
            productList = productRepository.findBySizeAndLength(size, length);
        } else if (size != null && propertyType != null) {
            productList = productRepository.findBySizeAndPropertyType(size, propertyType);
        } else if (length != null && propertyType != null) {
            productList = productRepository.findByLengthAndPropertyType(length, propertyType);
        } else if (style != null) {
            productList = productRepository.findByStyle(style);
        } else if (size != null) {
            productList = productRepository.findBySize(size);
        } else if (length != null) {
            productList = productRepository.findByLength(length);
        } else if (propertyType != null) {
            productList = productRepository.findByPropertyType(propertyType);
        } else {
            productList = productRepository.findAll();
        }
        List<ProductResponse> productResponseList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            Product product = productList.get(i);
            ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
            List<File> fileList = fileRepository.findByProductId(product.getId());
            List<FileResponse> fileResponses = fileList.stream().map(file -> modelMapper.map(file, FileResponse.class)).toList();
            productResponse.setFileResponseList(fileResponses);
            productResponseList.add(productResponse);
        }
        return ResponseEntity.ok(productResponseList);
    }
}






