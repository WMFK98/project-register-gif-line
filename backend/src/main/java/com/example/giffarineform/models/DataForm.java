package com.example.giffarineform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DataForm {
    private String id;
    private String prefix;
    private String name;
    private String birthDate;
    private String phone;
    private String address;
    private String zipCode;
    @JsonIgnore
    private MultipartFile cardImg;
}
