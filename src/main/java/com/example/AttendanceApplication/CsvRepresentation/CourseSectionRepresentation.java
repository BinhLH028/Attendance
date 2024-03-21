package com.example.AttendanceApplication.CsvRepresentation;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseSectionRepresentation {

//    @CsvBindByName(column = "hoc_ki")
//    private Integer semester;
//
//    @CsvBindByName(column = "nam_hoc")
//    private Integer year;

    @CsvBindByName(column = "ma_mon_hoc")
    private String courseCode;

    @CsvBindByName(column = "giang_vien")
    private String teachers;
}
