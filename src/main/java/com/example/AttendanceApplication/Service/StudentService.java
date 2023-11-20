package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public ResponseEntity getStudents() {
        List<Student> students = studentRepository.findAll();
        if (students.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(students,HttpStatus.OK);
    }
}
