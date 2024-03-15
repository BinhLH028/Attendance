package com.example.AttendanceApplication.DTO;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import com.example.AttendanceApplication.Model.Section;
import com.example.AttendanceApplication.Model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSectionDTO {

    private int id;

    private int courseId;

    private int sectionId;

    private String courseCode;

    private String courseName;

    private Set<StudentEnrolled> studentEnrolleds;

    private Student student;
}
