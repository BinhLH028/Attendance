package com.example.AttendanceApplication.Controller;

import com.example.AttendanceApplication.Request.AssignClassRequest;
import com.example.AttendanceApplication.Service.TeacherTeachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/API/teacher_teach")
public class TeacherTeachController {

    @Autowired
    private TeacherTeachService teacherTeachService;


    @GetMapping(value = "")
    public ResponseEntity<?> getTeacherListByCSId(@RequestParam int id) {
        try {
            return teacherTeachService.getTeacherListByCSId(id);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/new")
    public ResponseEntity<?> assignTeachers(@RequestBody AssignClassRequest request) {
        try {
            return teacherTeachService.assignTeachers(request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/update")
    public ResponseEntity<?> updateAssign(@RequestBody AssignClassRequest request) {
        try {
            return teacherTeachService.updateAssign(request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/delete")
    public ResponseEntity<?> deleteTeaching(@RequestBody List<Integer> request) {
        try {
            return teacherTeachService.deleteAssign(request);
        } catch (RuntimeException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
