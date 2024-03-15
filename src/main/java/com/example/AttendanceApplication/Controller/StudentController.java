package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping(value = "/API/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "")
    public ResponseEntity<?> getStudents(@RequestParam Optional<Integer> courseId) {
        try {
            if (courseId.isPresent()) {
                return new ResponseEntity(studentService.getStudentsByCourse(courseId.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity(studentService.getStudents(), HttpStatus.OK);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
