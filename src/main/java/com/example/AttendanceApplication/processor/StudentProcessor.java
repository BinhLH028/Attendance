package com.example.AttendanceApplication.processor;

import com.example.AttendanceApplication.Common.Const;
import com.example.AttendanceApplication.DTO.StudentDTO;
import com.example.AttendanceApplication.Enum.Role;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Service.StudentBatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class StudentProcessor implements ItemProcessor<StudentDTO,Student> {

    @Autowired
    private StudentBatchService studentBatchService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Format formatter = new SimpleDateFormat("dd/MM/yyyy");

    private boolean initialized = false;

    private boolean isValid = true;

    private Set<Integer> existingCodes;

    private String msg = "";

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        if (!initialized) {
            existingCodes = studentBatchService.getAllStudentCode();
            initialized = true;
        }
    }

    @Override
    public Student process(StudentDTO data) {

        String code = data.getUserCode();

        if (studentBatchService.isDuplicate(data.getUserCode())) {
            msg = "duplicate code: " + data.getUserCode() + " input";
            log.info(Const.CSV_MARKER, msg);
            isValid = false;
        }
        if (existingCodes.contains(code)) {
            msg = "the code: " + data.getUserCode() + " has already used";
            log.info(Const.DB_MARKER, msg);
            isValid = false;
        }

        if (!isValid) {
            throw new RuntimeException(msg);
        }

        return mappingStudent(data);
    }

    private Student mappingStudent(StudentDTO data) {

        Student student = new Student();

        student.setUserName(data.getUserName());
        student.setUsercode(data.getUserCode());
        student.setEmail(data.getUserCode() + "@vnu.edu.vn");
        student.setDob(data.getDob());
        student.setGender(data.getGender());

        student.setRole(Role.USER);
        student.setEnabled(true);
        String rawPw= "";
        try {
            rawPw = concatenateArrayElements(formatter.format(data.getDob()).split("/"));
        } catch (Exception e) {
            log.info("Error DOB: " + data.getUserCode());
            rawPw = String.valueOf(data.getUserCode());
        }
        student.setPassword(passwordEncoder.encode(rawPw));

        return student;
    }

    public String concatenateArrayElements(String[] array) {
        StringBuilder sb = new StringBuilder();
        for (String element : array) {
            sb.append(element);
        }
        return sb.toString();
    }

}
