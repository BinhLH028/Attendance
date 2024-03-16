package com.example.AttendanceApplication.Model.Relation;

import com.example.AttendanceApplication.Model.CommonEntity;
import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Section;
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
public class CourseSection extends CommonEntity {

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

    @OneToMany(mappedBy = "courseSection")
    @JsonIgnore
    @ToString.Exclude
    private Set<TeacherTeach> teacherTeachs;

    @OneToMany(mappedBy = "courseSection")
    @JsonIgnore
    @ToString.Exclude
    private Set<StudentEnrolled> studentEnrolleds;

    @Column(name = "isEnableAttendance", columnDefinition = "boolean default false")
    private boolean isEnableAttendance = false;

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
