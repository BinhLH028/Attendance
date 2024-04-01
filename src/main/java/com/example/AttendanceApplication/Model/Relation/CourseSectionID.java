package com.example.AttendanceApplication.Model.Relation;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CourseSectionID implements Serializable {
    private Integer id;

    private String team;
}
