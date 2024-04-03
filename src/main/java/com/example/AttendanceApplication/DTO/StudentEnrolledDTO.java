package com.example.AttendanceApplication.DTO;

import com.example.AttendanceApplication.Model.AttendanceSheet;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentEnrolledDTO {

    private Integer Id;

    private Student student;

//    private CourseSection courseSection;

//    private AttendanceSheet attendanceSheet;
}
