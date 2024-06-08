package com.example.giffarineform.services;
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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
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


    public synchronized Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.findStorageLocation.resolve(fileName).normalize();
            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found: " + fileName);
            }

            byte[] fileContent = Files.readAllBytes(filePath);

            // Schedule file deletion after 10 seconds
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.schedule(() -> {
                try {
                    Files.delete(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }, 30, TimeUnit.SECONDS);
            return new ByteArrayResource(fileContent);

        } catch (IOException e) {
            throw new RuntimeException("File operation error: " + fileName, e);
        }
    }


    public DataForm sendBroadcast(DataForm dataForm) {
        String fileName = StringUtils.cleanPath(dataForm.getCardImg().getOriginalFilename());
        String randomFileName = "";
        try {
            if (fileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence" + fileName);
            }
            String fileExtension = "";
            if (dataForm.getCardImg().getOriginalFilename().contains(".")) {
                fileExtension = dataForm.getCardImg().getOriginalFilename().substring(dataForm.getCardImg().getOriginalFilename().lastIndexOf("."));
            }
            randomFileName = generateRandomString(50) + fileExtension;
            Path targetLocation = this.findStorageLocation.resolve(randomFileName);
            Files.copy(dataForm.getCardImg().getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ".Please try again", ex);
        }

        Map<String, Object> messageText = new HashMap<>();
        messageText.put("type", "text");
        messageText.put("text", String.format("ชื่อ %s %s\nวันเกิด %s เบอโทร %s\nที่อยู่ %s รหัสไปรษณีย์ %s\nบัตรประชาชน %s",
                dataForm.getPrefix(), dataForm.getName(), dataForm.getBirthDate(),
                dataForm.getPhone(), dataForm.getAddress(), dataForm.getZipCode(),dataForm.getId()));

        Map<String, Object> imageIdCard = new HashMap<>();
        imageIdCard.put("type", "image");
        imageIdCard.put("originalContentUrl","https://kunmuay-giffrine.com/api/v1/line/image/"+randomFileName );
        imageIdCard.put("previewImageUrl","https://kunmuay-giffrine.com/api/v1/line/image/"+randomFileName );

//        Map<String, Object> imagePayment = new HashMap<>();
//        imagePayment.put("type", "image");
//        imagePayment.put("originalContentUrl","https://kunmuay-giffrine.com/api/v1/line/image/test.png" );
//        imagePayment.put("previewImageUrl","https://kunmuay-giffrine.com/api/v1/line/image/test.png" );

        Map<String, Object> data = new HashMap<>();
        data.put("messages", new Object[]{messageText,imageIdCard});

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + this.keyChannel);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);
            restTemplate.exchange(  this.url+ "/broadcast", HttpMethod.POST, requestEntity, String.class);
        return dataForm;
    }

    private String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }
}
