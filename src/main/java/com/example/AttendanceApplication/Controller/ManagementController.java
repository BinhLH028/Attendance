package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.DTO.FilterManagementDTO;
import com.example.AttendanceApplication.Service.ManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/management")
public class ManagementController {

    @Autowired
    private ManagementService managementService;

    @PostMapping(value = "")
    public ResponseEntity<?> getData(
            @RequestParam(value = "page", defaultValue = "0", required = false ) Integer page,
            @RequestParam(value = "ps", defaultValue = "10", required = false ) Integer size,
            @RequestBody FilterManagementDTO filter) {
        try {
            return new ResponseEntity(managementService.getData(filter, page, size), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
