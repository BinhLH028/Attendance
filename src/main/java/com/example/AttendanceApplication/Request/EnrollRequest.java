package com.example.AttendanceApplication.Request;

import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollRequest {

    private List<Integer> studentIds;

    private Integer courseSectionId;

    private String team;

    public EnrollRequest(List<Integer> studentIds, Integer courseSectionId) {
        this.studentIds = studentIds;
        this.courseSectionId = courseSectionId;
    }
}
