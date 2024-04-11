package com.example.AttendanceApplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDTO {

    private Integer userId;

    private String userName;

    private String email;

    private Date dob;

    private int teacherTeachId;

    private String phone;

    private String department;

    public TeacherDTO(Integer userId, String userName, String email, int teacherTeachId) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.teacherTeachId = teacherTeachId;
    }

    public TeacherDTO(Integer userId, String userName, String email, Date dob) {
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.dob = dob;
    }

    public TeacherDTO(String userName, String email, String phone, String department) {
        this.userName = userName;
        this.email = email;
        this.phone = phone;
        this.department = department;
    }

    public TeacherDTO(String userName, String email, Date dob, String phone, String department) {
        this.userName = userName;
        this.email = email;
        this.dob = dob;
        this.phone = phone;
        this.department = department;
    }
}
