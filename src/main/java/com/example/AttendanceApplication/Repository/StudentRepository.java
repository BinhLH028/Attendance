package com.example.AttendanceApplication.Repository;

import com.example.AttendanceApplication.DTO.FilterManagementDTO;
import com.example.AttendanceApplication.Model.Student;
import com.example.AttendanceApplication.Response.StudentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {


    @Query("""
            SELECT s FROM Student s 
            WHERE (s.userId = :studentId)
                AND s.delFlag = false 
            """)
    Student findStudentByStudentId(Integer studentId);

    Student findStudentByUserIdAndDelFlagFalse(Integer id);

    @Query("""
            SELECT s.usercode FROM Student s 
            WHERE (s.delFlag = false )
            """)
    List<Integer> findAllStudentCode();

    @Query("""
            SELECT DISTINCT 
            new com.example.AttendanceApplication.Response.StudentResponse (
                s.userId,
                s.usercode,
                s.userName,
                c.courseCode,
                c.courseName,
                cs.id,
                cs.team,
                att.totalAbsence
            )
            FROM Student s 
            JOIN StudentEnrolled enroll
                ON s = enroll.student
            JOIN CourseSection cs
                ON cs = enroll.courseSection
            JOIN Course c
                ON c = cs.course
            JOIN AttendanceSheet att
                ON att.studentEnrolled = enroll
            WHERE (:#{#filter.sectionId} IS NULL OR cs.section.sectionId = :#{#filter.sectionId})
            AND (:#{#filter.userCode} IS NULL OR s.usercode LIKE %:#{#filter.userCode}%)
            AND (:#{#filter.username} IS NULL OR s.userName LIKE %:#{#filter.username}%) 
            AND (:#{#filter.courseCode} IS NULL OR c.courseCode LIKE %:#{#filter.courseCode}%) 
            AND (:#{#filter.courseName} IS NULL OR c.courseName LIKE %:#{#filter.courseName}%) 
            AND (:#{#filter.team} IS NULL OR cs.team LIKE %:#{#filter.team}%) 
            AND (:#{#filter.totalAbsence} IS NULL OR att.totalAbsence = :#{#filter.totalAbsence})
            AND (s.delFlag = false )
            AND (enroll.delFlag = false )
            AND (cs.delFlag = false )
            AND (c.delFlag = false )
            AND (att.delFlag = false )
            """)
    Page<StudentResponse> findStudentsWithFilter(Pageable page, FilterManagementDTO filter);
}

