package com.example.AttendanceApplication.DTO;

import com.example.AttendanceApplication.Model.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSectionDTO {
    private int id;

    private int courseId;

    private int sectionId;

    private String courseCode;

    private String courseName;
}
