package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Request.CourseSectionRequest;
import com.example.AttendanceApplication.Request.UserRequest;
import com.example.AttendanceApplication.Service.CourseSectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @GetMapping(value = "{id}")
    public ResponseEntity<?> getCourseSectionBySection(@PathVariable int id,
                                                       @RequestParam(name = "page", required = false, defaultValue = "0") int page
    ) {
        try {
            return new ResponseEntity(courseSectionService.getCourseSectionBySection(id, page), HttpStatus.OK);
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
            @RequestBody CourseSectionRequest request) {
        try {
            return courseSectionService.addCourseSection(request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<?> uploadCourse(@RequestParam MultipartFile file,
                                          @RequestParam Integer sectionId) {
        try {
            return courseSectionService.uploadCourse(file, sectionId);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}


