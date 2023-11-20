package com.example.AttendanceApplication.Model.Relation;

import com.example.AttendanceApplication.Model.AppUser;
import com.example.AttendanceApplication.Model.AttendanceSheet;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Model.Teacher;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "attendanceSheet")
@Table(name = "student_enrolled")
public class StudentEnrolled {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer Id;

//    @OneToMany(mappedBy = "studentEnrolled")
//    @ToString.Exclude
//    private List<CourseSection> courseSection;

//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "student_id")
//    @ToString.Exclude
//    private Student student;

//    @OneToOne(mappedBy = "studentEnrolled")
//    @ToString.Exclude
//    private CourseSection courseSection;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @ToString.Exclude
//    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @ToString.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id")
    @JsonIgnore
    @ToString.Exclude
    private CourseSection courseSection;

    @OneToOne(mappedBy = "studentEnrolled",cascade = CascadeType.ALL)
    @ToString.Exclude
    private AttendanceSheet attendanceSheet;

    public StudentEnrolled(Student student, CourseSection courseSection) {
        this.student = student;
        this.courseSection = courseSection;
    }
}
