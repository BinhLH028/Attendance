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
@EqualsAndHashCode(exclude="appUser")
@Table(name = "student")
public class Student{

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private AppUser appUser;

//    @OneToOne(mappedBy = "student",cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private StudentEnrolled studentEnrolled;

    @OneToMany(mappedBy = "student")
    @ToString.Exclude
    private List<StudentEnrolled> studentEnrolled;

//    @OneToMany(mappedBy = "student")
//    @ToString.Exclude
//    private List<CourseSection> courseSection;


    @Column(name = "user_name")
    private String userName;

    @Column(name = "pass_word")
    private String password;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "schoolyear")
    private Integer schoolyear;

    public Student(Integer userId) {
        this.userId = userId;
    }

    public Student(String userName, String password, String email, Role role) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

}
