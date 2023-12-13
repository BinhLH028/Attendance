package com.example.AttendanceApplication.DTO;

import com.example.AttendanceApplication.Model.AttendanceSheet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDataDTO {

    private int userId;
    private int userCode;
    private String userName;
    private String dob;
    private int sheetId;
    private AttendanceSheet attendanceSheet;
}
