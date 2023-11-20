package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SectionRepository extends JpaRepository<Section, Integer> {

    @Query("""
            SELECT COUNT(*) FROM Section sec WHERE
            sec.year = :year
            """)
    int semesterPerYear(Integer year);

    @Query("""
            SELECT sec FROM Section sec WHERE
            (sec.year = :year)
            AND (sec.semester = :semester)
            """)
    Optional<Section> findBySemesterAndYear(Integer semester, Integer year);


    @Query("""
            SELECT sec FROM Section sec WHERE
            (sec.sectionId = :sectionId)
            """)
    Section findSectionById(Integer sectionId);
}
