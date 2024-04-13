package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.DTO.StudentDTO;
import com.example.AttendanceApplication.DTO.TeacherDTO;
import com.example.AttendanceApplication.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/API/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "")
    public ResponseEntity<?> getStudents(@RequestParam(value = "page", defaultValue = "0", required = false ) Integer page) {
        try {
            return new ResponseEntity(studentService.getStudents(page), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "")
    public ResponseEntity<?> getStudentsWithFilter(
            @RequestParam(value = "page", defaultValue = "0", required = false ) Integer page,
            @RequestBody StudentDTO filter
    ) {
        try {
            return new ResponseEntity(studentService.getStudentsWithFilter(page, filter), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllStudents() {
        try {
            return new ResponseEntity(studentService.getAllStudents(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Integer id) {
        try {
            return new ResponseEntity(studentService.getStudentById(id), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
