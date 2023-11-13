package com.example.clothesshop.controller;
import com.example.clothesshop.dao.entity.User;
import com.example.clothesshop.dao.repository.UserRepository;
import com.example.clothesshop.enums.*;
import com.example.clothesshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;
    private final UserRepository userRepository;

    public ProductController(ProductService productService, UserRepository userRepository) {
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @PostMapping("/saveProduct/{userId}")
    public ResponseEntity<?> saveProduct(@RequestParam(name = "images", required = false) List<MultipartFile> images,
                                         @RequestParam("name") String name,
                                         @RequestParam("color") String color,
                                         @RequestParam("price") Double price,
                                         @RequestParam(name = "size") String size,
                                         @RequestParam(name = "style") Style style,
                                         @RequestParam(name = "length") Length length,
                                         @RequestParam(name = "propertyType") PropertyType propertyType,
                                         @PathVariable(name = "userId") Long userId) throws IOException {
        return productService.saveProduct(images, name, color, price, userId, size, style, length, propertyType);
    }

    @GetMapping("/getAllProducts/{userId}")
    public ResponseEntity<?> getAllProducts(@PathVariable("userId") Long userId){
        return productService.getAllProducts(userId);
    }

    @GetMapping("{userId}/getByIdProduct/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable("productId") Long productId,
                                            @PathVariable("userId") Long userId){

        return productService.getProductById(productId, userId);

    }

    @PutMapping("{userId}/updateProduct/{productId}")
    public ResponseEntity<?> updateProduct(
            @RequestParam(name = "images", required = false) List<MultipartFile> images,
            @RequestParam String name,
            @RequestParam String color,
            @RequestParam Double price,
            @RequestParam String size,
            @RequestParam Style style,
            @RequestParam(name = "length") Length length,
            @RequestParam(name = "propertyType") PropertyType propertyType,
            @PathVariable("productId") Long productId,
            @PathVariable("userId") Long userId) {
        try {
            return productService.updateProduct(images, name, color, price,
                                                productId, userId,size,
                                                style, length, propertyType);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/filter/{userId}")
    public ResponseEntity<?> filterProducts(
            @RequestParam(required = false) Style style,
            @RequestParam(required = false) Size size,
            @RequestParam(required = false) Length length,
            @RequestParam(required = false) PropertyType propertyType,
            @PathVariable(name = "userId") Long userId
    ) {

        return productService.filterProducts(style, size, length,propertyType, userId);
    }

    @DeleteMapping("/{userId}/delete-product/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long userId, @PathVariable Long productId){
        Optional<User> optionalUser = userRepository.findById(userId);

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not Found");
        }

        if (!optionalUser.get().getUserRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Admin role is required to perform this operation.");
        }
        productService.deleteProductById(productId);
        return ResponseEntity.noContent().build();
    }

}


