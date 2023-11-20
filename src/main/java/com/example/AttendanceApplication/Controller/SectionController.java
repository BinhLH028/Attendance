package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Request.SectionRequest;
import com.example.AttendanceApplication.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/API/section")
public class SectionController {

    @Autowired
    private SectionService sectionService;

    @Autowired
    MessageSource messageSource;
    @PostMapping(value = "/new")
    public ResponseEntity<?> addSection(
            @RequestBody SectionRequest request) {
        try {
            return sectionService.addSection(request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
