package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Request.AssignClassRequest;
import com.example.AttendanceApplication.Service.TeacherTeachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/API/teacher_teach")
public class TeacherTeachController {

    @Autowired
    private TeacherTeachService teacherTeachService;


    @PostMapping(value = "/new")
    public ResponseEntity<?> assignTeachers(@RequestBody AssignClassRequest request) {
        try {
            return teacherTeachService.assignTeachers(request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
