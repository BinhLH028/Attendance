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
}
