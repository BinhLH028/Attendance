package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.StudentRepository;
import com.example.AttendanceApplication.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {


    @Autowired
    private TeacherRepository teacherRepository;

    public ResponseEntity getTeachers() {
        List<Teacher> teachers = teacherRepository.findAll();
        if (teachers.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(teachers,HttpStatus.OK);
    }
}
