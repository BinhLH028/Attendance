package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Request.UserRequest;
import com.example.AttendanceApplication.Service.CourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping(value = "/API/course_section")
public class CourseSectionController {

    @Autowired
    private CourseSectionService courseSectionService;

    @GetMapping(value = "")
    public ResponseEntity<?> getCourseSection() {
        try {
            return new ResponseEntity(courseSectionService.getCourseSection(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "{sectionId}")
    public ResponseEntity<?> getCourseSectionByUser(@PathVariable int sectionId,@RequestBody UserRequest user) {
        try {
            return new ResponseEntity(courseSectionService.getCourseSectionByUser(sectionId,user), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/new")
    public ResponseEntity<?> addCourseSection(
            @RequestBody CourseSection request) {
        try {
            return courseSectionService.addCourseSection(request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}


