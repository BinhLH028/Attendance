package com.example.AttendanceApplication.Model.Relation;


import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Model.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "teacher_teach")
public class TeacherTeach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer Id;

//    @OneToMany(mappedBy = "teacherTeach")
//    @ToString.Exclude
//    private List<CourseSection> courseSection;

//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "teacher_id")
//    @ToString.Exclude
//    private Teacher teacher;

//    @OneToOne(mappedBy = "teacherTeach")
//    @ToString.Exclude
//    private CourseSection courseSection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id")
    @ToString.Exclude
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id")
    @JsonIgnore
    @ToString.Exclude
    private CourseSection courseSection;

    public TeacherTeach(Teacher teacher, CourseSection courseSection) {
        this.teacher = teacher;
        this.courseSection = courseSection;
    }
}
