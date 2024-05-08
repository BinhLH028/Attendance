package com.example.AttendanceApplication.Model.Relation;

import com.example.AttendanceApplication.Model.AttendanceSheet;
import com.example.AttendanceApplication.Model.CommonEntity;
import com.example.AttendanceApplication.Model.Student;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

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
    @JsonIgnore
    @ToString.Exclude
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        StudentEnrolled that = (StudentEnrolled) o;
//        return Objects.equals(student.getUserId(), that.student.getUserId()) &&
//                Objects.equals(courseSection.getId(), that.courseSection.getId()) &&
//                Objects.equals(courseSection.getTeam(), that.courseSection.getTeam());
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(student.getUserId(), courseSection.getId());
//    }
}
