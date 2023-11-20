package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query("""
            SELECT c FROM Course c WHERE
            (c.courseId = :id)
            """)
    Course findCourseById(Integer id);

    @Query("""
            SELECT c FROM Course c WHERE
            (c.courseCode = :courseCode)
            """)
    Optional<Course> findByCourseCode(String courseCode);
}
