package com.example.giffarineform.models;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DataForm {
    private String prefix;
    private String name;
    private String birthDate;
    private String phone;
    private String address;
    private String zipCode;
    private String idCard;

}
