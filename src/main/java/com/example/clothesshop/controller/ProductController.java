package com.example.clothesshop.controller;
import com.example.clothesshop.enums.Length;
import com.example.clothesshop.enums.PropertyType;
import com.example.clothesshop.enums.Size;
import com.example.clothesshop.enums.Style;
import com.example.clothesshop.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/saveProduct/{userId}")
    public ResponseEntity<?> saveProduct(@RequestParam(name = "images", required = false) List<MultipartFile> images,
                                         @RequestParam("name") String name,
                                         @RequestParam("color") String color,
                                         @RequestParam("price") Double price,
                                         @RequestParam(name = "size") Size size,
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
            @RequestParam Size size,
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

}


