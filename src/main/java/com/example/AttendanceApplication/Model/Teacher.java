package com.example.AttendanceApplication.Model;

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
@Table(name = "teacher")
public class Teacher extends AppUser {

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<TeacherTeach> teacherTeachs;

    @Column(name = "department")
    private String department = "";

    public Teacher(String userName, String email, String phone, Date dob, String department) {
        super(userName, email, phone, dob);
        this.department = department;
    }

    public Teacher(String userName, String password, Boolean enabled, String email, String phone, Date dob, Role role, String department) {
        super(userName, password, enabled, email, phone, dob, role);
        this.department = department;
    }
}
