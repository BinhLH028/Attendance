package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Relation.CourseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Integer> {

    @Query("""
            SELECT cs FROM CourseSection cs WHERE
            (cs.course.courseId = :courseId)
            AND (cs.section.sectionId = :sectionId)
            """)
    CourseSection findbySectionAndCourse(Integer sectionId, Integer courseId);

    @Query("""
            SELECT cs FROM CourseSection cs WHERE
            (cs.id = :id)
            """)
    CourseSection findbyCSId(Integer id);
}
