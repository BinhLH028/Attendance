package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Request.EnrollRequest;
import com.example.AttendanceApplication.Service.StudentEnrolledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/API/student_enrolled")
public class StudentEnrolledController {

    @Autowired
    private StudentEnrolledService studentEnrolledService;

    @PostMapping(value = "/new")
    public ResponseEntity<?> addNewEnroll(@RequestBody EnrollRequest request) {
        try {
            return studentEnrolledService.addNewEnrolls(request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
