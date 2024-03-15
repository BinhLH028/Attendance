package com.example.AttendanceApplication.DTO;

import com.example.AttendanceApplication.Enum.Role;
import com.example.AttendanceApplication.Model.AppUser;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Integer userId;

    private Date dob;

    private int age;

    private int gender;

    private String userName;

    private int usercode;

    private String email;

    private Integer schoolyear;
}
