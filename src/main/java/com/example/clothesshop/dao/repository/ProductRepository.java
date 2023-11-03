package com.example.clothesshop.dao.repository;

import com.example.clothesshop.dao.entity.Product;
import com.example.clothesshop.enums.Length;
import com.example.clothesshop.enums.PropertyType;
import com.example.clothesshop.enums.Size;
import com.example.clothesshop.enums.Style;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStyle(Style style);
    List<Product> findBySize(Size size);
    List<Product> findByLength(Length length);
    List<Product> findByPropertyType(PropertyType propertyType);

    List<Product> findByStyleAndSize(Style style, Size size);
    List<Product> findByStyleAndLength(Style style, Length length);
    List<Product> findByStyleAndPropertyType(Style style, PropertyType propertyType);

    List<Product> findBySizeAndLength(Size size, Length length);
    List<Product> findBySizeAndPropertyType(Size size, PropertyType propertyType);

    List<Product> findByLengthAndPropertyType(Length length, PropertyType propertyType);


    List<Product> findByStyleAndSizeAndLength(Style style, Size size, Length length);
    List<Product> findByStyleAndSizeAndPropertyType(Style style, Size size, PropertyType propertyType);
    List<Product> findByStyleAndLengthAndPropertyType(Style style, Length length, PropertyType propertyType);

    List<Product> findBySizeAndLengthAndPropertyType(Size size, Length length, PropertyType propertyType);

    List<Product> findByStyleAndSizeAndLengthAndPropertyType(Style style, Size size, Length length, PropertyType propertyType);
}

