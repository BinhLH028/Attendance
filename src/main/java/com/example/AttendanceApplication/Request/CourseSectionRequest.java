package com.example.AttendanceApplication.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseSectionRequest {

    private Integer sectionId;

    private List<Integer> courseIds;
}
