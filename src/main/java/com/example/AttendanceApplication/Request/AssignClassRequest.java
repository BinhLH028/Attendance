package com.example.AttendanceApplication.Request;

import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Teacher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignClassRequest {

    private List<Integer> teacherIds;
    private Integer courseSection;
//    private String team;
}
