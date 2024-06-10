package com.example.giffarineform.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data

public class DataForm {
    @NotNull
    @Size(max = 13,min = 13)
    private String id;
    @NotNull
    @NotBlank
    @Size(max = 10)
    private String prefix;
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String name;
    @NotNull
    @NotBlank
    @Size(max = 100)
    private String birthDate;
    @Size(max = 10,min = 10)
    @NotNull
    @NotBlank
    private String phone;
    @NotNull
    @NotBlank
    @Size(max = 300)
    private String address;
    @NotNull
    @NotBlank
    @Size(max = 5,min = 5)
    private String zipCode;
    @NotNull
    @JsonIgnore
    private MultipartFile cardImg;
    @NotNull
    @JsonIgnore
    private MultipartFile paymentImg;
}
