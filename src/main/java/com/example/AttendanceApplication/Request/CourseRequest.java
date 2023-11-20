package com.example.AttendanceApplication.Request;

import com.example.AttendanceApplication.Model.Relation.CourseSection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequest {
    private String courseCode;

    private String courseName;

    private Set<CourseSection> courseSections;

    public CourseRequest(String courseCode, String courseName) {
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

}
