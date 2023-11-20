package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("""
            SELECT s FROM Teacher s WHERE
            (s.userId = :teacherId)
            """)
    Teacher findStudentByStudentId(Integer teacherId);
}
