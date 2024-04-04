package com.example.AttendanceApplication.DTO;

import com.example.AttendanceApplication.Model.AttendanceSheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDataDTO {

    private int userId;

    private String userCode;

    private String userName;

    private String dob;

    private int sheetId;

    private String team;

    private AttendanceSheet attendanceSheet;
}
