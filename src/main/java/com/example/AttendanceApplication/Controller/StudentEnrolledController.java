package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Request.AssignClassRequest;
import com.example.AttendanceApplication.Request.EnrollRequest;
import com.example.AttendanceApplication.Service.StudentEnrolledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/student_enrolled")
public class StudentEnrolledController {

    @Autowired
    private StudentEnrolledService studentEnrolledService;

    @GetMapping(value = "")
    public ResponseEntity<?> getStudentListByCSId(@RequestParam int id) {
        try {
            return studentEnrolledService.getStudentListByCSId(id);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/new")
    public ResponseEntity<?> addNewEnroll(@RequestBody EnrollRequest request) {
        try {
            return studentEnrolledService.addNewEnrolls(request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> updateEnroll(@RequestBody EnrollRequest request) {
        try {
            return studentEnrolledService.updateEnroll(request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadEnroll(@RequestParam MultipartFile file,
                                          @RequestParam Integer sectionId) {
        try {
            return studentEnrolledService.uploadEnroll(file, sectionId);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
