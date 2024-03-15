package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {


    @Query("""
            SELECT s FROM Student s WHERE
            (s.userId = :studentId)
            """)
    Student findStudentByStudentId(Integer studentId);

    @Query("""
            select s from Student s
            join StudentEnrolled se on s.userId = se.student.userId
            join CourseSection cs on cs.Id = se.courseSection.Id
            where cs.Id = :courseSectionId
            """)
    List<Student> findStudentByCourseSectionId(Integer courseSectionId);
}
