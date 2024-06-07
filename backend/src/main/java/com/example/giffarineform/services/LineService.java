package com.example.giffarineform.services;

import com.example.giffarineform.LineProperties;
import com.example.giffarineform.models.DataForm;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

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


    public DataForm sendBroadcast(DataForm dataForm) {
        Map<String, Object> messageText = new HashMap<>();
        messageText.put("type", "text");
        messageText.put("text", String.format("ชื่อ %s %s\nวันเกิด %s เบอโทร %s\nที่อยู่ %s รหัสไปรษณีย์ %s\nบัตรประชาชน %s",
                dataForm.getPrefix(), dataForm.getName(), dataForm.getBirthDate(),
                dataForm.getPhone(), dataForm.getAddress(), dataForm.getZipCode(),dataForm.getId()));

        Map<String, Object> imageIdCard = new HashMap<>();
        imageIdCard.put("type", "image");
        imageIdCard.put("originalContentUrl","https://project-register-gif-line.vercel.app/images/ex-id-card.jpg" );
        imageIdCard.put("previewImageUrl","https://project-register-gif-line.vercel.app/images/ex-id-card.jpg" );

        Map<String, Object> imagePayment = new HashMap<>();
        imagePayment.put("type", "image");
        imagePayment.put("originalContentUrl"," https://project-register-gif-line.vercel.app/images/ex-payment.jpeg" );
        imagePayment.put("previewImageUrl"," https://project-register-gif-line.vercel.app/images/ex-payment.jpeg" );

        Map<String, Object> data = new HashMap<>();
        data.put("messages", new Object[]{messageText,imageIdCard,imagePayment});

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization", "Bearer " + this.keyChannel);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);
       ResponseEntity<String> test = restTemplate.exchange(  this.url+ "/broadcast", HttpMethod.POST, requestEntity, String.class);
        System.out.println(test);
        return dataForm;
    }
}
