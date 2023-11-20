package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {


    @Query("""
            SELECT s FROM Student s WHERE
            (s.userId = :studentId)
            """)
    Student findStudentByStudentId(Integer studentId);
}
