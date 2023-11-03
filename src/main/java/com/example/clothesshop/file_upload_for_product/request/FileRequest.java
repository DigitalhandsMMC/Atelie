package com.example.clothesshop.file_upload_for_product.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileRequest {

    private String name;
    private String contentType;
    private byte[] fileData;

}