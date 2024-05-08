package com.example.AttendanceApplication.Model;

import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.example.AttendanceApplication.Model.Relation.StudentEnrolled;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "attendancesheet")
public class AttendanceSheet extends CommonEntity {

    @Id
    @JsonIgnore
    @Column(name = "id")
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnore
    @ToString.Exclude
    private StudentEnrolled studentEnrolled;

    @Column(name = "total_absence")
    private Integer totalAbsence = 0;

    @Column(name = "lecture1",columnDefinition = "boolean default null")
    private Boolean lecture1;

    @Column(name = "lecture2",columnDefinition = "boolean default null")
    private Boolean lecture2;

    @Column(name = "lecture3",columnDefinition = "boolean default null")
    private Boolean lecture3;

    @Column(name = "lecture4",columnDefinition = "boolean default null")
    private Boolean lecture4;

    @Column(name = "lecture5",columnDefinition = "boolean default null")
    private Boolean lecture5;

    @Column(name = "lecture6",columnDefinition = "boolean default null")
    private Boolean lecture6;

    @Column(name = "lecture7",columnDefinition = "boolean default null")
    private Boolean lecture7;

    @Column(name = "lecture8",columnDefinition = "boolean default null")
    private Boolean lecture8;

    @Column(name = "lecture9",columnDefinition = "boolean default null")
    private Boolean lecture9;

    @Column(name = "lecture10",columnDefinition = "boolean default null")
    private Boolean lecture10;

    @Column(name = "lecture11",columnDefinition = "boolean default null")
    private Boolean lecture11;

    @Column(name = "lecture12",columnDefinition = "boolean default null")
    private Boolean lecture12;

    @Column(name = "lecture13",columnDefinition = "boolean default null")
    private Boolean lecture13;

    @Column(name = "lecture14",columnDefinition = "boolean default null")
    private Boolean lecture14;

    @Column(name = "lecture15",columnDefinition = "boolean default null")
    private Boolean lecture15;

    @Column(name = "lectureoption1",columnDefinition = "boolean default null")
    private Boolean lectureOption1;

    @Column(name = "lectureoption2",columnDefinition = "boolean default null")
    private Boolean lectureOption2;

    public AttendanceSheet(StudentEnrolled studentEnrolled) {
        this.studentEnrolled = studentEnrolled;
    }



}
