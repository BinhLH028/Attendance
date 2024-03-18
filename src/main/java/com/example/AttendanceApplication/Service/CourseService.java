package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    MessageSource messageSource;

    private String msg = "";

    public ResponseEntity getCourses() {
        List<Course> courses = courseRepository.findAll();
        if (courses.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(courses,HttpStatus.OK);
    }

    public ResponseEntity addCourse(Course course) {
        if (validateCourse(course)) {
            courseRepository.save(course);
            msg = messageSource.getMessage("C01",
                    new String[]{course.getCourseCode()}, Locale.getDefault());
            return new ResponseEntity(msg, HttpStatus.OK);
        }
        return new ResponseEntity(msg,HttpStatus.BAD_REQUEST);
    }

    private boolean validateCourse(Course course) {
        boolean isValid = true;
        Optional<Course> temp = courseRepository.findByCourseCode(course.getCourseCode());

        if (!temp.isEmpty()) {
            isValid = false;
            msg = messageSource.getMessage("C02",
                    new String[]{course.getCourseCode()}, Locale.getDefault());
        }

        return  isValid;
    }

    public ResponseEntity editCourse(Integer id, Course course){
        Course temp = courseRepository.findCourseById(id);

        if (temp == null) {
            msg = messageSource.getMessage("C03",
                    new String[]{course.getCourseCode()}, Locale.getDefault());
            return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
        }
        temp.setCourseCode(course.getCourseCode());
        temp.setCourseName(course.getCourseName());
        courseRepository.save(temp);
        msg = messageSource.getMessage("C04",
                new String[]{course.getCourseCode()}, Locale.getDefault());
        return new ResponseEntity(msg, HttpStatus.OK);
    }

    public ResponseEntity getCoursesById(Integer id) {

        Course course = courseRepository.findCourseByCourseIdAndDelFlagFalse(id);
        if (course != null) {
            return new ResponseEntity(course, HttpStatus.OK);
        }
        msg = messageSource.getMessage("C03",
                new String[]{course.getCourseCode()}, Locale.getDefault());
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity getCourseBySectionId(int sectionId) {
        List<Course> courses = courseRepository.findCourseBySectionId(sectionId);
        if (courses.size() > 0) {
            return new ResponseEntity(courses, HttpStatus.OK);
        }
        msg = messageSource.getMessage("C05",
                new String[]{}, Locale.getDefault());
        return new ResponseEntity(msg, HttpStatus.BAD_REQUEST);
    }
}
