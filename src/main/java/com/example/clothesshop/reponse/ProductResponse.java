package com.example.clothesshop.reponse;


import com.example.clothesshop.enums.Length;
import com.example.clothesshop.enums.PropertyType;
import com.example.clothesshop.enums.Size;
import com.example.clothesshop.enums.Style;
import com.example.clothesshop.file_upload_for_product.response.FileResponse;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {

    private  Long id;

    private String name;
    private Double price;

    private String color;

    private String size;
    private Style style;
    private Length length;

    private PropertyType propertyType;

    private List<FileResponse> fileResponseList;
}
