package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Mapper.StudentMapper;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    public ResponseEntity getStudents() {
        List<Student> students = studentRepository.findAll();
        if (students.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(students,HttpStatus.OK);
    }

    public ResponseEntity getStudentsByCourse(Integer courseId) {
        List<Student> students = studentRepository.findStudentByCourseSectionId(courseId);

        if (students.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(students.stream()
                .map(s -> studentMapper.convertToDto(s))
                .collect(Collectors.toList())
                , HttpStatus.OK);
    }
}
