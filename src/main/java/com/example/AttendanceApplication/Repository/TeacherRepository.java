package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.Model.Course;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query("""
            SELECT s FROM Teacher s 
            WHERE (s.userId = :teacherId)
                AND s.delFlag = false 
            """)
    Teacher findStudentByStudentId(Integer teacherId);

    Teacher findTeacherByUserIdAndDelFlagFalse(Integer id);

    List<Teacher> findByDelFlagFalse();
}
