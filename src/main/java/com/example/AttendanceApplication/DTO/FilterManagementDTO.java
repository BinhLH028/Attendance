package com.example.AttendanceApplication.DTO;

import lombok.Data;

@Data
public class FilterManagementDTO {

    private String userCode;
    private String username;
    private String courseCode;
    private String courseName;
    private String teacherName;
    private String team;
    private Integer totalAbsence;
}
