package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.CsvRepresentation.CourseRepresentation;
import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Repository.CourseRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    MessageSource messageSource;

    private String msg = "";

    private List<String> resultMsg = new ArrayList<>();

    public ResponseEntity getCourses() {
        List<Course> courses = courseRepository.findAll();
        if (courses.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(courses,HttpStatus.OK);
    }

    public ResponseEntity addCourse(Course course) {
        if (validateCourse(course)) {
            courseRepository.saveAndFlush(course);
            msg = messageSource.getMessage("C01",
                    new String[]{course.getCourseCode()}, Locale.getDefault());
            return new ResponseEntity(msg, HttpStatus.OK);
        }
        return new ResponseEntity(msg,HttpStatus.BAD_REQUEST);
    }

    private boolean validateCourse(Course course) {
        boolean isValid = true;
        Course temp = courseRepository.findByCourseCode(course.getCourseCode());

        if (temp != null) {
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

    public ResponseEntity<?> uploadCourse(MultipartFile file) throws IOException {
        resultMsg.clear();
        Set<Course> courses = parseCsv(file);

        if (!resultMsg.isEmpty()) {
            return new ResponseEntity(resultMsg, HttpStatus.BAD_REQUEST);
        }
        courses.forEach(course -> {
            Course temp = courseRepository.findByCourseCode(course.getCourseCode());
            if (temp != null) {
                msg = messageSource.getMessage("C02",
                        new String[]{course.getCourseCode()}, Locale.getDefault());
                resultMsg.add(msg);
            }
        });
        if (!resultMsg.isEmpty()) {
            return new ResponseEntity(resultMsg, HttpStatus.BAD_REQUEST);
        }

        courseRepository.saveAll(courses);
        msg = messageSource.getMessage("C08",
                new String[]{String.valueOf(courses.size())}, Locale.getDefault());

        return new ResponseEntity(msg, HttpStatus.OK);
    }

    private Set<Course> parseCsv(MultipartFile file) throws IOException {
        try(Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            HeaderColumnNameMappingStrategy<CourseRepresentation> strategy =
                    new HeaderColumnNameMappingStrategy<>();
            strategy.setType(CourseRepresentation.class);
            CsvToBean<CourseRepresentation> csvToBean =
                    new CsvToBeanBuilder<CourseRepresentation>(reader)
                            .withMappingStrategy(strategy)
                            .withIgnoreEmptyLine(true)
                            .withIgnoreLeadingWhiteSpace(true)
                            .build();
            AtomicInteger index = new AtomicInteger(1);
            return csvToBean.parse()
                    .stream()
                    .map(csvLine -> {
                        String code = csvLine.getCourseCode();
                        String name = csvLine.getCourseName();
                                if (code == null || code == "") {
                                    msg = messageSource.getMessage("C06",
                                            new String[]{String.valueOf(index.getAndIncrement())}, Locale.getDefault());
                                    code = "";
                                    resultMsg.add(msg);
                                }
                                if (name == null || name == "") {
                                    msg = messageSource.getMessage("C07",
                                            new String[]{String.valueOf(index.getAndIncrement())}, Locale.getDefault());
                                    name = "";
                                    resultMsg.add(msg);
                                }
                                return new Course(code, name);
                            }
                    )
                    .collect(Collectors.toSet());
        }
    }
}
