package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/API/teacher")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping(value = "")
    public ResponseEntity<?> getTeachers(@RequestParam(value = "page", defaultValue = "0", required = false ) Integer page) {
        try {
            return new ResponseEntity(teacherService.getTeachers(page), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllTeachers() {
        try {
            return new ResponseEntity(teacherService.getAllTeachers(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTeacherById(@PathVariable("id") Integer id) {
        try {
            return new ResponseEntity(teacherService.getTeacherById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
