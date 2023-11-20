package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/API/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping(value = "")
    public ResponseEntity<?> getTeachers() {
        try {
            return new ResponseEntity(teacherService.getTeachers(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
