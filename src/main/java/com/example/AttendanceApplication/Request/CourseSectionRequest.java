package com.example.AttendanceApplication.Request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseSectionRequest {

    private Integer sectionId;

    private List<Integer> courseIds;

    private String team;

    private String room;

    private Integer courseId;

    private List<Integer> teachersId;

}
