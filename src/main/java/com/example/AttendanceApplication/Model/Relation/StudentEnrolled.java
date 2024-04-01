package com.example.AttendanceApplication.Model.Relation;

import com.example.AttendanceApplication.Model.AttendanceSheet;
import com.example.AttendanceApplication.Model.CommonEntity;
import com.example.AttendanceApplication.Model.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude = "attendanceSheet")
@Table(name = "student_enrolled")
public class StudentEnrolled extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @ToString.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "course_section_id", referencedColumnName = "id"),
            @JoinColumn(name = "team", referencedColumnName = "team")
            })
    @JsonIgnore
    @ToString.Exclude
    private CourseSection courseSection;

    @OneToOne(mappedBy = "studentEnrolled", cascade = CascadeType.ALL)
    @ToString.Exclude
    private AttendanceSheet attendanceSheet;

    public StudentEnrolled(Student student, CourseSection courseSection) {
        this.student = student;
        this.courseSection = courseSection;
    }
}
