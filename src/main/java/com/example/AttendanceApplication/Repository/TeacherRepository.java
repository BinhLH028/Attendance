package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.DTO.TeacherDTO;
import com.example.AttendanceApplication.Model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    @Query("""
        SELECT DISTINCT 
            new com.example.AttendanceApplication.DTO.TeacherDTO (
                teacher.userId,
                teacher.userName,
                teacher.email,
                teacher.dob,
                teacher.phone,
                teacher.department
            )
        FROM Teacher teacher
        WHERE (:#{#filter.userName} IS NULL OR LOWER(teacher.userName) LIKE %:#{#filter.userName})
            AND (:#{#filter.email} IS NULL OR LOWER(teacher.email) LIKE %:#{#filter.email}%)
            AND (:#{#filter.phone} IS NULL OR LOWER(teacher.phone) LIKE %:#{#filter.phone}%) 
            AND (:#{#filter.department} IS NULL OR LOWER(teacher.department) LIKE %:#{#filter.department}%) 
            AND teacher.delFlag = false 
    """)
    Page<TeacherDTO> findTeachersWithFilter(Pageable pageable, TeacherDTO filter);

    @Query("""
        SELECT teacher
        FROM Teacher teacher
        WHERE (teacher.userId IN :teachersId)
            AND teacher.delFlag = false 
    """)
    List<Teacher> findByIdInAndDelFlagFalse(List<Integer> teachersId);

    @Query("""
    SELECT teacher FROM Teacher teacher
        WHERE (:name IS NULL OR LOWER(teacher.userName) LIKE %:name%)
        AND teacher.delFlag = false
        ORDER BY teacher.userName
    """)
    List<Teacher> findByNameFilter(String name, PageRequest pageRequest);
}
