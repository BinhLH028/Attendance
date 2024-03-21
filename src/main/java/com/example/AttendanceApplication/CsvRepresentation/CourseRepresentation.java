package com.example.AttendanceApplication.CsvRepresentation;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRepresentation {

    @CsvBindByName(column = "ma_mon_hoc")
    private String courseCode;

    @CsvBindByName(column = "ten_mon_hoc")
    private String courseName;
}
