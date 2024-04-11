package com.example.AttendanceApplication.CsvRepresentation;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeacherRepresentation {

    @CsvBindByName(column = "ho_va_ten")
    private String userName;

    @CsvBindByName(column = "email")
    private String email;

    @CsvBindByName(column = "so_dien_thoai")
    private String phone;

    @CsvBindByName(column = "ngay_sinh")
    private String dob;

    @CsvBindByName(column = "khoa")
    private String department;
}
