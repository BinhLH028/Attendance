package com.example.AttendanceApplication.CsvRepresentation;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentRepresentation {

    @CsvBindByName(column = "msv")
    private String userCode;

    @CsvBindByName(column = "ho_va_ten")
    private String userName;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "so_dien_thoai")
    private String phone;

    @CsvBindByName(column = "ngay_sinh")
    private String dob;

    @CsvBindByName(column = "khoa")
    private String schoolYear;
}
