package com.example.AttendanceApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {

    private Integer userId;

    private String userName;

    private String userCode;

    private String email;

    private Date dob;

    private Integer gender;

    private String phone;

    private String schoolYear;

    public StudentDTO(String userName, String userCode, Date dob, Integer gender) {
        this.userName = userName;
        this.userCode = userCode;
        this.dob = dob;
        this.gender = gender;
    }

    public StudentDTO(String userName, String userCode, String email, Date dob, Integer gender) {
        this.userName = userName;
        this.email = email;
        this.userCode = userCode;
        this.dob = dob;
        this.gender = gender;
    }

    public StudentDTO(String userName, String userCode, String email, Date dob, Integer gender, String phone) {
        this.userName = userName;
        this.email = email;
        this.userCode = userCode;
        this.dob = dob;
        this.gender = gender;
        this.phone = phone;
    }

    public StudentDTO(Integer userId, String userName, String userCode, String email, Date dob) {
        this.userId = userId;
        this.userName = userName;
        this.userCode = userCode;
        this.email = email;
        this.dob = dob;
    }

    public StudentDTO(String userName, String userCode, String email, String phone, String schoolYear) {
        this.userName = userName;
        this.userCode = userCode;
        this.email = email;
        this.phone = phone;
        this.schoolYear = schoolYear;
    }

    public StudentDTO(Integer userId, String userName, String userCode, String email, Date dob, String phone, String schoolYear) {
        this.userId = userId;
        this.userName = userName;
        this.userCode = userCode;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
        this.schoolYear = schoolYear;
    }
}
