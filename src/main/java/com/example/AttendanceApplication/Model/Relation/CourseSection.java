package com.example.AttendanceApplication.Model.Relation;

import com.example.AttendanceApplication.Model.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course_section")
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer Id;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private Section section;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "student_id")
//    @ToString.Exclude
//    private Student student;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "teacher_id")
//    @ToString.Exclude
//    private Teacher teacher;

//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "student_id")
//    @ToString.Exclude
//    private StudentEnrolled studentEnrolled;
//
//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "teacher_id")
//    @ToString.Exclude
//    private TeacherTeach teacherTeach;

    @OneToMany(mappedBy = "courseSection")
    @JsonIgnore
    @ToString.Exclude
    private Set<TeacherTeach> teacherTeachs;

    @OneToMany(mappedBy = "courseSection")
    @JsonIgnore
    @ToString.Exclude
    private Set<StudentEnrolled> studentEnrolleds;

//    @OneToOne(mappedBy = "courseSection",cascade = CascadeType.ALL)
//    @ToString.Exclude
//    private AttendanceSheet attendanceSheet;

    public CourseSection(Section section, Course course) {
        this.section = section;
        this.course = course;
    }

    public CourseSection(Integer id, Section section, Course course) {
        Id = id;
        this.section = section;
        this.course = course;
    }

    public CourseSection(Integer id) {
        Id = id;
    }
}
