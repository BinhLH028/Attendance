package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.DTO.TeacherDTO;
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
    Teacher findTeacherByTeacherId(Integer teacherId);

    Teacher findTeacherByUserIdAndDelFlagFalse(Integer id);

    @Query("""
    SELECT new com.example.AttendanceApplication.DTO.TeacherDTO (
        a.userId,
        a.userName,
        a.email,
        a.dob
    ) from Teacher a 
    WHERE a.delFlag = false 
    """)
    List<TeacherDTO> findByDelFlagFalse();


    @Query("""
        SELECT DISTINCT
            new com.example.AttendanceApplication.DTO.TeacherDTO(
                s.userId,
                s.userName,
                s.email,
                tt.id
            )
        FROM Teacher s
        JOIN TeacherTeach tt ON s.userId = tt.teacher.userId
        WHERE (tt.courseSection.id = :CSId)
            AND s.delFlag = false 
            AND tt.delFlag = false 
    """)
    List<TeacherDTO> findByCSId(int CSId);

    Teacher findByEmailAndDelFlagFalse(String t);

    @Query("""
        SELECT teacher.userId
        FROM Teacher teacher
        WHERE (teacher.email = :t)
            AND teacher.delFlag = false 
    """)
    Integer findUserIdByEmailAndDelFlagFalse(String t);
}
