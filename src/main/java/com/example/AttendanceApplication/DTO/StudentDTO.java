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
}
