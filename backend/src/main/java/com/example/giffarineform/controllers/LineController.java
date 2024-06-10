package com.example.giffarineform.controllers;

import com.example.giffarineform.exceptions.ErrorResponse;
import com.example.giffarineform.models.DataForm;
import com.example.giffarineform.services.LineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestController
@CrossOrigin(origins = "${value.url.cross.origin}")
@RequestMapping("/v1/line")
public class LineController {
    @Autowired
    private LineService service;

    @PostMapping("/broadcast")
    public ResponseEntity<Object> broadcastLine( @ModelAttribute @Valid DataForm dataForm){
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlerMethodValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(null,HttpStatus.BAD_REQUEST.value(),null,"Validation error. Check 'errors' field for details. statusForCreateOrUpdate",  request.getDescription(false));
        for (FieldError fieldError : ex.getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
