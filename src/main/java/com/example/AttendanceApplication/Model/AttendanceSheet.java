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
    private Integer Id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    @JsonIgnore
    @ToString.Exclude
    private StudentEnrolled studentEnrolled;

    @Column(name = "lecture1",columnDefinition = "boolean default false")
    private Boolean lecture1 = false;

    @Column(name = "lecture2",columnDefinition = "boolean default false")
    private Boolean lecture2 = false;

    @Column(name = "lecture3",columnDefinition = "boolean default false")
    private Boolean lecture3 = false;

    @Column(name = "lecture4",columnDefinition = "boolean default false")
    private Boolean lecture4 = false;

    @Column(name = "lecture5",columnDefinition = "boolean default false")
    private Boolean lecture5 = false;

    @Column(name = "lecture6",columnDefinition = "boolean default false")
    private Boolean lecture6 = false;

    @Column(name = "lecture7",columnDefinition = "boolean default false")
    private Boolean lecture7 = false;

    @Column(name = "lecture8",columnDefinition = "boolean default false")
    private Boolean lecture8 = false;

    @Column(name = "lecture9",columnDefinition = "boolean default false")
    private Boolean lecture9 = false;

    @Column(name = "lecture10",columnDefinition = "boolean default false")
    private Boolean lecture10 = false;

    @Column(name = "lecture11",columnDefinition = "boolean default false")
    private Boolean lecture11 = false;

    @Column(name = "lecture12",columnDefinition = "boolean default false")
    private Boolean lecture12 = false;

    @Column(name = "lecture13",columnDefinition = "boolean default false")
    private Boolean lecture13 = false;

    @Column(name = "lecture14",columnDefinition = "boolean default false")
    private Boolean lecture14 = false;

    @Column(name = "lecture15",columnDefinition = "boolean default false")
    private Boolean lecture15 = false;

//    @Column(name = "lectureoption1",columnDefinition = "boolean default false")
//    private Boolean lectureOption1 = false;
//
//    @Column(name = "lectureoption2",columnDefinition = "boolean default false")
//    private Boolean lectureOption2 = false;

    public AttendanceSheet(StudentEnrolled studentEnrolled) {
        this.studentEnrolled = studentEnrolled;
    }



}
