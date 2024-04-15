package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.Common.Const;
import com.example.AttendanceApplication.DTO.TeacherDTO;
import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {


    @Autowired
    private TeacherRepository teacherRepository;

    public ResponseEntity getTeachers(int page) {
        Page<Teacher> teachers = teacherRepository.findAll(PageRequest.of(page, 10));
        if (teachers.isEmpty() ) {
            return new ResponseEntity("No Data",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(teachers,HttpStatus.OK);
    }

    public ResponseEntity getTeacherById(Integer id) {
        Teacher teacher = teacherRepository.findTeacherByUserIdAndDelFlagFalse(id);

        if (teacher != null) {
            return new ResponseEntity(teacher, HttpStatus.OK);
        }
        return new ResponseEntity("can't find teacher with id " + id,HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity getAllTeachers() {
        List<TeacherDTO> teachers = teacherRepository.findByDelFlagFalse();
        if (teachers.isEmpty() ) {
            return new ResponseEntity("No Data",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(teachers,HttpStatus.OK);
    }

    public ResponseEntity getTeachersWithFilter(Integer page, TeacherDTO filter) {
        Const.convertFieldsToLowerCase(filter);
        Page<TeacherDTO> teachers = teacherRepository.findTeachersWithFilter(PageRequest.of(page, 10), filter);
//        if (teachers.isEmpty() ) {
//            return new ResponseEntity("No Data",HttpStatus.BAD_REQUEST);
//        }
        return new ResponseEntity(teachers,HttpStatus.OK);
    }

    public ResponseEntity getTeachersByName(String name) {
        List<Teacher> teachers = teacherRepository.findByNameFilter(name, PageRequest.of(0, Const.PAGE_SIZE));
        if (teachers.size() <= 0 ) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(teachers,HttpStatus.OK);
    }
}
