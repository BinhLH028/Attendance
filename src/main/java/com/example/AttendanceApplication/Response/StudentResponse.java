package com.example.AttendanceApplication.Response;

import com.example.AttendanceApplication.Model.AttendanceSheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {

    private Integer userId;

    private String userCode;

    private String username;

    private String courseCode;

    private String courseName;

    private String teacherName;

    private Integer csId;

    private String team;

    private Integer totalAbsence;

    private AttendanceSheet attendanceSheet;

    public StudentResponse(Integer userId, String userCode, String username, String courseCode, String courseName, String teacherName, String team, Integer totalAbsence) {
        this.userId = userId;
        this.userCode = userCode;
        this.username = username;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.teacherName = teacherName;
        this.team = team;
        this.totalAbsence = totalAbsence;
    }

    public StudentResponse(Integer userId, String userCode, String username, String courseCode, String courseName, Integer csId, String team) {
        this.userId = userId;
        this.userCode = userCode;
        this.username = username;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.csId = csId;
        this.team = team;
    }
}
