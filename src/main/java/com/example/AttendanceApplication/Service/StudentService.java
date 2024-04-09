package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.DTO.StudentDTO;
import com.example.AttendanceApplication.Mapper.StudentMapper;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public ResponseEntity getStudents(int page) {
        Page<Student> students = studentRepository.findAll(PageRequest.of(page, 10));
        if (students.isEmpty()) {
            return new ResponseEntity("No student data.",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(students,HttpStatus.OK);
    }

    public ResponseEntity getStudentById(Integer id) {
        Student student = studentRepository.findStudentByUserIdAndDelFlagFalse(id);
        if (student != null) {
            return new ResponseEntity(student, HttpStatus.OK);
        }
        return new ResponseEntity("can't find student with id " + id,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity getAllStudents() {
        List<StudentDTO> students = studentRepository.findByDelFlagFalse();
        if (students.isEmpty() ) {
            return new ResponseEntity("No Data",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(students,HttpStatus.OK);
    }
}

