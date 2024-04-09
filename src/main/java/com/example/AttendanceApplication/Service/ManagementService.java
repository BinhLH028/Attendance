package com.example.AttendanceApplication.Service;

import com.example.AttendanceApplication.DTO.FilterManagementDTO;
import com.example.AttendanceApplication.DTO.TeacherDTO;
import com.example.AttendanceApplication.Model.AttendanceSheet;
import com.example.AttendanceApplication.Model.Teacher;
import com.example.AttendanceApplication.Repository.AttendanceRepository;
import com.example.AttendanceApplication.Repository.StudentRepository;
import com.example.AttendanceApplication.Repository.TeacherTeachRepository;
import com.example.AttendanceApplication.Response.StudentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ManagementService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherTeachRepository ttRepo;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    MessageSource messageSource;

    private String msg = "";

    public ResponseEntity getData(FilterManagementDTO filter, Integer pageNum) {
        Pageable page = PageRequest.of(pageNum, 10);
        Page<StudentResponse> responses = studentRepository.findStudentsWithFilter(PageRequest.of(pageNum, 10), filter);
        responses.forEach(o -> {
            setupListData(o);
        });
        return new ResponseEntity<>(responses, HttpStatus.OK);

    }

    private void setupListData(StudentResponse o) {
        List<TeacherDTO> teachers = ttRepo.findTeachersByCSId(o.getCsId());
        if (teachers.isEmpty()) {
            msg = messageSource.getMessage("TT04", null, Locale.getDefault());
            log.info("[ManagementData]  " + msg);
        }
        String teacherNames = teachers.stream()
                .map(TeacherDTO::getUserName)
                .collect(Collectors.joining(", "));
        o.setTeacherName(teacherNames);

        AttendanceSheet sheet = attendanceRepository.findSheetByStudentIdAndCSId(o.getUserId(), o.getCsId());
        o.setAttendanceSheet(sheet);
    }
}
