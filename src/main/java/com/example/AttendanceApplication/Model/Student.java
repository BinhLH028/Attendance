package com.example.AttendanceApplication.Model;

import com.example.AttendanceApplication.Auth.Token.Token;
import com.example.AttendanceApplication.Enum.Role;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import com.example.AttendanceApplication.Model.Relation.TeacherTeach;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student extends AppUser {

    @Column(name = "user_code")
    private int usercode;

    @OneToMany(mappedBy = "student")
    @ToString.Exclude
    private List<StudentEnrolled> studentEnrolled;

    @Column(name = "schoolyear")
    private Integer schoolyear;

}
