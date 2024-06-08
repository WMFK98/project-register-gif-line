package com.example.giffarineform.controllers;

import com.example.giffarineform.models.DataForm;
import com.example.giffarineform.services.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v1/line")
public class LineController {
    @Autowired
    private LineService service;

    @PostMapping("/broadcast")
    public ResponseEntity<Object> broadcastLine(@ModelAttribute DataForm dataForm){
        return ResponseEntity.ok(service.sendBroadcast(dataForm));
    }

    @GetMapping("/test")
    public ResponseEntity<Object> test(){
        return ResponseEntity.ok("I'm here");
    }

    @GetMapping("/image/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename){
        Resource file = service.loadFileAsResource(filename);

        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(file);
    }
}
