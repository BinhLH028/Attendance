package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Request.SectionRequest;
import com.example.AttendanceApplication.Service.CourseService;
import com.example.AttendanceApplication.Service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/API/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    MessageSource messageSource;

    @GetMapping(value = "")
    public ResponseEntity<?> getCourses() {
        try {
            return new ResponseEntity(courseService.getCourses(),HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(value = "/new")
    public ResponseEntity<?> addCourse(
            @RequestBody Course course) {
        try {
            return courseService.addCourse(course);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<?> editCourse(@PathVariable int id,@RequestBody Course course) {
        try {
            return courseService.editCourse(id,course);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
