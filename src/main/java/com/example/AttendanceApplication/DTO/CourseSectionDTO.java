package com.example.AttendanceApplication.DTO;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseSectionDTO {

    private int id;

    private int courseId;

    private int sectionId;

    private String courseCode;

    private String courseName;

    private String team;

    private String room;

    private List<TeacherDTO> teacherName;

    public CourseSectionDTO(int id, int courseId, int sectionId, String courseCode, String courseName) {
        this.id = id;
        this.courseId = courseId;
        this.sectionId = sectionId;
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public CourseSectionDTO(int id, int courseId, int sectionId, String courseCode, String courseName, String room, String team) {
        this.id = id;
        this.courseId = courseId;
        this.sectionId = sectionId;
        this.courseCode = courseCode;
        this.room = room;
        this.courseName = courseName;
        this.team = team;
    }
}
