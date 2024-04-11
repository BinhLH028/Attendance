package com.example.AttendanceApplication.Model;

import com.example.AttendanceApplication.Auth.Token.Token;
import com.example.AttendanceApplication.Enum.Role;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import com.example.AttendanceApplication.Model.Relation.TeacherTeach;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student extends AppUser {

    @Column(name = "user_code")
    private String usercode = "";

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    @ToString.Exclude
    @JsonIgnore
    private List<StudentEnrolled> studentEnrolled;

    @Column(name = "schoolyear")
    private String schoolyear = "";

    public Student(String userName, String password, Boolean enabled, String email, String phone, Date dob, Role role, String usercode, String schoolyear) {
        super(userName, password, enabled, email, phone, dob, role);
        this.usercode = usercode;
        this.schoolyear = schoolyear;
    }
}
