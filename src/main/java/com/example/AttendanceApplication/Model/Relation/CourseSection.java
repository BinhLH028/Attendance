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
//@IdClass(CourseSectionID.class)
public class CourseSection extends CommonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "team", unique = true)
    private String team = "CL";

    @Column(name = "room")
    private String room;

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
//    @JsonIgnore
    @ToString.Exclude
    private Set<StudentEnrolled> studentEnrolleds;

    @Column(name = "isEnableAttendance", columnDefinition = "boolean default false")
    private boolean isEnableAttendance = false;

    @Column(name = "startWeek", columnDefinition = "Integer default 1")
    private Integer startWeek = 1;

    public CourseSection(Section section, Course course) {
        this.section = section;
        this.course = course;
    }

    public CourseSection(Integer id, Section section, Course course) {
        this.id = id;
        this.section = section;
        this.course = course;
    }

    public CourseSection( Section section, Course course, String team) {
        this.section = section;
        this.course = course;
        this.team = team;
    }


}
