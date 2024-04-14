package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("""
            SELECT c FROM Course c 
            WHERE (c.courseId = :id)
                AND c.delFlag = false 
            """)
    Course findCourseById(Integer id);

    @Query("""
            SELECT c FROM Course c 
            WHERE (c.courseId IN :ids)
                AND c.delFlag = false 
            """)
    List<Course> findCourseByIdIn(List<Integer> ids);

    @Query("""
            SELECT c FROM Course c 
            WHERE (c.courseCode = :courseCode)
                AND c.delFlag = false 
            """)
    Course findByCourseCode(String courseCode);

    Course findCourseByCourseIdAndDelFlagFalse(Integer id);

    @Query("""
        SELECT c FROM Course c
        JOIN CourseSection cs ON c.courseId = cs.course.courseId
            WHERE (cs.section.sectionId = :sectionId)
            AND c.delFlag = false
            AND cs.delFlag = false
    """)
    List<Course> findCourseBySectionId(int sectionId);

    List<Course> findByDelFlagFalse();
}
