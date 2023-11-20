package com.example.AttendanceApplication.Model;

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
@Table(name = "teacher")
public class Teacher {

    @Id
    @Column(name = "user_id")
    private Integer userId;

    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private AppUser appUser;

//    @OneToOne(mappedBy = "teacher",cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private TeacherTeach teacherTeach;

    @OneToMany(mappedBy = "teacher")
    @ToString.Exclude
    private List<TeacherTeach> teacherTeachs;

//    @OneToMany(mappedBy = "teacher")
//    @ToString.Exclude
//    private List<CourseSection> courseSection;

}
