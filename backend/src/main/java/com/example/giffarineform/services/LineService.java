package com.example.giffarineform.services;
import com.example.giffarineform.exceptions.InvalidFieldInputException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import com.example.giffarineform.LineProperties;
import com.example.giffarineform.models.DataForm;
import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Getter
public class LineService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String url;
    private final String keyChannel;
    private final Path findStorageLocation;

    @Autowired
    public LineService(LineProperties lineProperties){
        this.url = lineProperties.getUri();
        this.keyChannel = lineProperties.getKeyChannel();
        this.findStorageLocation = Paths.get(lineProperties.getUploadDir()).toAbsolutePath().normalize();
        try {
            if (!Files.exists(this.findStorageLocation)) {
                Files.createDirectories(this.findStorageLocation);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }


    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.findStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found" + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File operation error" + fileName, e);
        }
    }

    private String storeRandomFileName(MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        String randomFileName = "";
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence" + fileName);
            }
            String fileExtension = "";
            if (file.getOriginalFilename().contains(".")) {
                fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            }
            randomFileName = generateRandomString(32) + fileExtension;
            Path targetLocation = this.findStorageLocation.resolve(randomFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File stored: " + targetLocation.toString());
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ".Please try again", ex);
        }
        return  randomFileName;
    }

    public DataForm sendBroadcast(DataForm dataForm) {
        if(dataForm.getCardImg().isEmpty()) throw new InvalidFieldInputException("cardImg","is must not be null");
        if(dataForm.getCardImg().isEmpty()) throw new InvalidFieldInputException("paymentImg","is must not be null");
        String cardImgName = storeRandomFileName(dataForm.getCardImg());
        String paymentImgName = storeRandomFileName(dataForm.getPaymentImg());
        Map<String, Object> messageText = new HashMap<>();
        messageText.put("type", "text");
        messageText.put("text", String.format("ชื่อ %s %s\nวันเกิด %s\nเบอโทร %s\nที่อยู่ %s\nรหัสไปรษณีย์ %s\nบัตรประชาชน %s",
                dataForm.getPrefix(), dataForm.getName(), dataForm.getBirthDate(),
                dataForm.getPhone(), dataForm.getAddress(), dataForm.getZipCode(), dataForm.getId()));

        Map<String, Object> imageIdCard = new HashMap<>();
        imageIdCard.put("type", "image");
        imageIdCard.put("originalContentUrl", "https://kunmuay-giffrine.com/api/v1/line/image/" + cardImgName);
        imageIdCard.put("previewImageUrl", "https://kunmuay-giffrine.com/api/v1/line/image/" + cardImgName);

        Map<String, Object> imagePayment = new HashMap<>();
        imagePayment.put("type", "image");
        imagePayment.put("originalContentUrl", "https://kunmuay-giffrine.com/api/v1/line/image/" + paymentImgName);
        imagePayment.put("previewImageUrl", "https://kunmuay-giffrine.com/api/v1/line/image/" + paymentImgName);

        Map<String, Object> data = new HashMap<>();
        data.put("messages", new Object[]{messageText, imageIdCard, imagePayment});

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + this.keyChannel);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(this.url + "/broadcast", HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Broadcast sent successfully");
            } else {
                System.out.println("Failed to send broadcast, status code: " + response.getStatusCode());
                removeFile(cardImgName);
                removeFile(paymentImgName);
            }
        }  catch (RestClientException ex) {
            removeFile(cardImgName);
            removeFile(paymentImgName);
        }
        return dataForm;
    }


    private void removeFile(String filename) {
        try {
            Path filePath = this.findStorageLocation.resolve(filename).normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new RuntimeException("File not found " + filename);
            }
        } catch (IOException e) {
            throw new RuntimeException("File operlation error: " + filename, e);
        }
    }

    private String generateRandomString(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }
}
