package com.example.AttendanceApplication.Model;

import com.example.AttendanceApplication.Model.Relation.CourseSection;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EqualsAndHashCode(exclude="courseSection")
@Table(name = "section")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Integer sectionId;

    @Column(name = "semester")
    private Integer semester;

    @Column(name = "year")
    private Integer year;

    @OneToMany(mappedBy = "section")
    @JsonIgnore
    private Set<CourseSection> courseSections;

    public Section(Integer semester, Integer year) {
        this.semester = semester;
        this.year = year;
    }
}
