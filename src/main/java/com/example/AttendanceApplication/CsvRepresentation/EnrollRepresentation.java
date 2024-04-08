package com.example.AttendanceApplication.CsvRepresentation;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EnrollRepresentation {

    @CsvBindByName(column = "ma_mon_hoc")
    private String courseCode;

    @CsvBindByName(column = "nhom")
    private String team;

    @CsvBindByName(column = "msv")
    private String userCode;

    @CsvBindByName(column = "sinh_vien")
    private String userName;
}
