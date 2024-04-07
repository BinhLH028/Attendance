package com.example.AttendanceApplication.Request;

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
    private Integer courseSectionId;
    private String team;

    public AssignClassRequest(List<Integer> teacherIds, Integer courseSectionId) {
        this.teacherIds = teacherIds;
        this.courseSectionId = courseSectionId;
    }
}
