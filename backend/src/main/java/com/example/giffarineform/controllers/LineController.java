package com.example.giffarineform.controllers;

import com.example.giffarineform.models.DataForm;
import com.example.giffarineform.services.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/v1/line")
public class LineController {
    @Autowired
    private LineService service;

    @PostMapping("/broadcast")
    public ResponseEntity<Object> broadcastLine(@RequestBody DataForm dataForm){
        return ResponseEntity.ok(service.sendBroadcast(dataForm));
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file = service.loadFileAsResource(filename);
        String fileName = file.getFilename();
        String extension = fileName.substring(fileName.length()-3);
        if(extension.equalsIgnoreCase("pdf"))
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(file);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }
}
